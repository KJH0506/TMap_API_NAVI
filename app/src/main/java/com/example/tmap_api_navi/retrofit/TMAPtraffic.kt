package com.example.tmap_api_navi.retrofit

data class TMAPtraffic(
    val metaData: MetaData
) {
    data class MetaData(
        val plan: Plan,
        val requestParameters: RequestParameters
    ) {
        data class Plan(
            val itineraries: List<Itinerary>
        ) {
            data class Itinerary(
                val fare: Fare,
                val pathType: Int,
                val totalDistance: Int,
                val totalTime: Int,
                val totalWalkDistance: Int,
                val totalWalkTime: Int,
                val transferCount: Int
            ) {
                data class Fare(
                    val regular: Regular
                ) {
                    data class Regular(
                        val currency: Currency,
                        val totalFare: Int
                    ) {
                        data class Currency(
                            val currency: String,
                            val currencyCode: String,
                            val symbol: String
                        )
                    }
                }
            }
        }

        data class RequestParameters(
            val endX: String,
            val endY: String,
            val reqDttm: String,
            val startX: String,
            val startY: String
        )
    }
}