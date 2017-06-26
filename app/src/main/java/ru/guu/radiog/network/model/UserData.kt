package ru.guu.radiog.network.model


import org.parceler.Parcel

/**
 * Created by dmitry on 23.06.17.
 */
@Parcel
class UserData : DataModel() {

    var guid: String? = null
        internal set
    var cn: String? = null
        internal set
    var firstName: String? = null
        internal set
    var lastName: String? = null
        internal set
    var mail: String? = null
        internal set
    var distinguishedName: String? = null
        internal set
    var title: String? = null
        internal set

    override fun getEntryId(): Int {
        return 0
    }
}
