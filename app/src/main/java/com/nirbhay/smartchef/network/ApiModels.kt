package com.nirbhay.smartchef.network

// Gemini API Models
data class GeminiRequest(
    val contents: List<Content>
) {
    companion object {
        fun createContent(parts: List<Part>): Content {
            return Content(parts)
        }

        fun createPart(text: String): Part {
            return Part(text)
        }

        fun createSimpleRequest(prompt: String): GeminiRequest {
            return GeminiRequest(
                contents = listOf(
                    Content(
                        parts = listOf(Part(prompt))
                    )
                )
            )
        }
    }
}

data class Content(
    val parts: List<Part>
)

data class Part(
    val text: String
)

data class GeminiResponse(
    val candidates: List<Candidate>
)

data class Candidate(
    val content: ContentResponse
)

data class ContentResponse(
    val parts: List<PartResponse>
)

data class PartResponse(
    val text: String
)

// Pexels API Models
data class PexelsResponse(
    val photos: List<Photo>
)

data class Photo(
    val src: PhotoSrc
)

data class PhotoSrc(
    val medium: String,
    val small: String
)