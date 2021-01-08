package com.kazakago.cooking_planner.repository

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.FirestoreOptions
import com.kazakago.cooking_planner.model.CookPlan
import com.kazakago.cooking_planner.model.WeeklyCookPlan

class CookPlanRepository {

    private val firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder()
        .setProjectId("weekly-cook-plan")
        .setCredentials(GoogleCredentials.getApplicationDefault())
        .build()

    suspend fun getWeeklyCookPlan(): WeeklyCookPlan {
        return firestoreOptions.service.collection("weekly-cook-plan").let {
            WeeklyCookPlan(
                getCookPlan(it.document("sunday")),
                getCookPlan(it.document("monday")),
                getCookPlan(it.document("tuesday")),
                getCookPlan(it.document("wednesday")),
                getCookPlan(it.document("thursday")),
                getCookPlan(it.document("friday")),
                getCookPlan(it.document("saturday")),
            )
        }
    }

    private fun getCookPlan(documentRef: DocumentReference): CookPlan {
        val recipe = documentRef.get().get()?.data?.get("recipe")?.toString() ?: ""
        return CookPlan(recipe)
    }

    suspend fun setWeeklyCookPlan(weeklyCookPlan: WeeklyCookPlan) {
        firestoreOptions.service.collection("weekly-cook-plan").apply {
            setCookPlan(document("sunday"), weeklyCookPlan.sunday)
            setCookPlan(document("monday"), weeklyCookPlan.monday)
            setCookPlan(document("tuesday"), weeklyCookPlan.tuesday)
            setCookPlan(document("wednesday"), weeklyCookPlan.wednesday)
            setCookPlan(document("thursday"), weeklyCookPlan.thursday)
            setCookPlan(document("friday"), weeklyCookPlan.friday)
            setCookPlan(document("saturday"), weeklyCookPlan.saturday)
        }
    }

    private fun setCookPlan(documentRef: DocumentReference, cookPlan: CookPlan) {
        documentRef.set(mapOf("recipe" to cookPlan.recipe))
    }

    suspend fun deleteWeeklyCookPlan() {
        firestoreOptions.service.collection("weekly-cook-plan").apply {
            deleteCookPlan(document("sunday"))
            deleteCookPlan(document("monday"))
            deleteCookPlan(document("tuesday"))
            deleteCookPlan(document("wednesday"))
            deleteCookPlan(document("thursday"))
            deleteCookPlan(document("friday"))
            deleteCookPlan(document("saturday"))
        }
    }

    private fun deleteCookPlan(documentRef: DocumentReference) {
        documentRef.delete()
    }
}