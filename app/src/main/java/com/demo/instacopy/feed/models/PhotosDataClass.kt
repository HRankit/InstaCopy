package com.demo.instacopy.feed.models


import com.google.gson.annotations.SerializedName

/*
* Created by hrank8t on 03-06-2020 - 16:44:49.
*/



data class Photos(

    @SerializedName("id") val id: String,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("updated_at") val updated_at: String,
    @SerializedName("promoted_at") val promoted_at: String,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int,
    @SerializedName("color") val color: String,
    @SerializedName("description") var description: String?,
    @SerializedName("alt_description") val alt_description: String,
    @SerializedName("urls") val urls: Urls,
    @SerializedName("links") val links: Links,
    @SerializedName("categories") val categories: List<String>,
    @SerializedName("likes") val likes: Int,
    @SerializedName("liked_by_user") val liked_by_user: Boolean,
    @SerializedName("current_user_collections") val current_user_collections: List<String>,
    @SerializedName("sponsorship") val sponsorship: Sponsorship,
    @SerializedName("user") val user: User,
    @SerializedName("il") var il: Boolean? // As state can be null. Hence the ? operator

)

data class Links(

    @SerializedName("self") val self: String,
    @SerializedName("html") val html: String,
    @SerializedName("photos") val photos: String,
    @SerializedName("likes") val likes: String,
    @SerializedName("portfolio") val portfolio: String,
    @SerializedName("following") val following: String,
    @SerializedName("followers") val followers: String
)


data class ProfileImage(
    @SerializedName("small") val small: String,
    @SerializedName("medium") val medium: String,
    @SerializedName("large") val large: String
)

data class Sponsor(

    @SerializedName("id") val id: String,
    @SerializedName("updated_at") val updated_at: String,
    @SerializedName("username") val username: String,
    @SerializedName("name") val name: String,
    @SerializedName("first_name") val first_name: String,
    @SerializedName("last_name") val last_name: String,
    @SerializedName("twitter_username") val twitter_username: String,
    @SerializedName("portfolio_url") val portfolio_url: String,
    @SerializedName("bio") val bio: String,
    @SerializedName("location") val location: String,
    @SerializedName("links") val links: Links,
    @SerializedName("profile_image") val profile_image: ProfileImage,
    @SerializedName("instagram_username") val instagram_username: String,
    @SerializedName("total_collections") val total_collections: Int,
    @SerializedName("total_likes") val total_likes: Int,
    @SerializedName("total_photos") val total_photos: Int,
    @SerializedName("accepted_tos") val accepted_tos: Boolean
)

data class Sponsorship(

    @SerializedName("impression_urls") val impression_urls: List<String>,
    @SerializedName("tagline") val tagline: String,
    @SerializedName("tagline_url") val tagline_url: String,
    @SerializedName("sponsor") val sponsor: Sponsor
)

data class Urls(

    @SerializedName("raw") val raw: String,
    @SerializedName("full") val full: String,
    @SerializedName("regular") val regular: String,
    @SerializedName("small") val small: String,
    @SerializedName("thumb") val thumb: String
)

data class User(

    @SerializedName("id") val id: String,
    @SerializedName("updated_at") val updated_at: String,
    @SerializedName("username") val username: String,
    @SerializedName("name") val name: String,
    @SerializedName("first_name") val first_name: String,
    @SerializedName("last_name") val last_name: String,
    @SerializedName("twitter_username") val twitter_username: String,
    @SerializedName("portfolio_url") val portfolio_url: String,
    @SerializedName("bio") val bio: String,
    @SerializedName("location") val location: String,
    @SerializedName("links") val links: Links,
    @SerializedName("profile_image") val profile_image: ProfileImage,
    @SerializedName("instagram_username") val instagram_username: String,
    @SerializedName("total_collections") val total_collections: Int,
    @SerializedName("total_likes") val total_likes: Int,
    @SerializedName("total_photos") val total_photos: Int,
    @SerializedName("accepted_tos") val accepted_tos: Boolean
)