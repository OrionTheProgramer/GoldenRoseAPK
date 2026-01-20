package com.example.golden_rose_apk.repository

import android.content.Context
import com.example.golden_rose_apk.config.ValorantApi
import com.example.golden_rose_apk.model.ProductFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private data class LocalProductSeed(
    val id: String,
    val name: String,
    val price: Double,
    val type: String,
    val category: String,
    val imageRes: String,
    val desc: String,
    val imageUrl: String? = null,
    val valorantWeaponId: String? = null
)

class LocalProductRepository(private val context: Context) {
    private val gson = Gson()
    private val weaponsApi: ValorantWeaponsApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://valorant-api.com/v1/")
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ValorantWeaponsApi::class.java)
    }

    suspend fun loadProducts(): List<ProductFirestore> = withContext(Dispatchers.IO) {
        val json = context.assets.open("products.json").bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<LocalProductSeed>>() {}.type
        val seeds: List<LocalProductSeed> = gson.fromJson(json, type) ?: emptyList()

        val weaponsById = fetchWeaponsById()

        return@withContext seeds.map { seed ->
            ProductFirestore(
                id = seed.id,
                name = seed.name,
                price = seed.price,
                type = seed.type,
                category = seed.category,
                image = resolveImage(seed, weaponsById),
                desc = seed.desc
            )
        }
    }

    private suspend fun fetchWeaponsById(): Map<String, String> {
        return runCatching {
            weaponsApi.getWeapons().data
                .mapNotNull { weapon ->
                    val icon = weapon.displayIcon?.takeIf { it.isNotBlank() }
                    if (icon != null) weapon.uuid to icon else null
                }
                .toMap()
        }.getOrElse { emptyMap() }
    }

    private fun resolveImage(seed: LocalProductSeed, weaponsById: Map<String, String>): String {
        val remoteUrl = seed.imageUrl?.takeIf { it.isNotBlank() }
        if (remoteUrl != null) {
            return remoteUrl
        }

        val valorantId = seed.valorantWeaponId?.takeIf { it.isNotBlank() }
        if (valorantId != null) {
            return weaponsById[valorantId] ?: ValorantApi.weaponImageUrl(valorantId)
        }

        return buildResourceUri(seed.imageRes)
    }

    private fun buildResourceUri(resourceName: String): String {
        val resourceId = context.resources.getIdentifier(resourceName, "drawable", context.packageName)
        return if (resourceId != 0) {
            "android.resource://${context.packageName}/drawable/$resourceName"
        } else {
            ""
        }
    }
}

private interface ValorantWeaponsApi {
    @GET("weapons")
    suspend fun getWeapons(): ValorantWeaponsResponse
}

private data class ValorantWeaponsResponse(
    val data: List<ValorantWeapon>
)

private data class ValorantWeapon(
    val uuid: String,
    val displayIcon: String?
)
