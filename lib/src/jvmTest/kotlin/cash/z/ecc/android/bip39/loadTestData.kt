package cash.z.ecc.android.bip39

import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

actual suspend fun loadTestData(): TestDataSet = Json.decodeFromString(MR.assets.BIP_0039_test_values.readText())