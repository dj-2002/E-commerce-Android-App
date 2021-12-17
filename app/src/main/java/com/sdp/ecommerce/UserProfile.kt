package com.sdp.ecommerce

class UserProfile {
    var username: String? = null
    var password: String? = null

    constructor() {
        //firebase constructor
    }

    constructor(username: String?, password: String?) {
        this.username = username
        this.password = password
    }
}