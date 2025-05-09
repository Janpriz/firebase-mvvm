package com.dang.boswos_firebase.model

class Upload{
    var name:String=""
    var description=""
    var price:String=""
    var imageUrl:String=""
    var id:String=""

    constructor(name:String,description:String,price:String,imageUrl:String,id:String){

        this.name=name
        this.description=description
        this.price=price
        this.imageUrl=imageUrl
        this.id=id

    }
    constructor()
}