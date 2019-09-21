package com.xdd.openweather.model.enumModel


@Suppress("SpellCheckingInspection", "unused")
enum class LocationEnum(override val remoteName: String) : IJsonEnum {
    YILAN("宜蘭縣"),
    HUALIEN("花蓮縣"),
    TAITUNG("臺東縣"),
    PENGHU("澎湖縣"),
    KINMEN("金門縣"),
    LIENCHIANG("連江縣"),
    TAIPEI("臺北市"),
    NEW_TAIPEI("新北市"),
    TAOYUAN("桃園市"),
    TAICHUNG("臺中市"),
    TAINAN("臺南市"),
    KAOHSIUNG("高雄市"),
    KEELUNG("基隆市"),
    HSINCHU_COUNTY("新竹縣"),
    HSINCHU_CITY("新竹市"),
    MIAOLI("苗栗縣"),
    CHANGHUA("彰化縣"),
    NANTOU("南投縣"),
    YUNLIN("雲林縣"),
    CHIAYI_COUNTY("嘉義縣"),
    CHIAYI_CITY("嘉義市"),
    PINGTUNG("屏東縣");

    companion object : IJsonEnum.ICompanion<LocationEnum> {
        override val nativeStringMap: Map<String, LocationEnum>
            get() = enumList.map { it.toString() to it }.toMap()
        override val enumList: List<LocationEnum>
            get() = values().toList()
    }
}