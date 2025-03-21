package cash.z.ecc.android.crypto

import cash.z.ecc.android.bip39.utils.toHex
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe

class Pbkdf2Sha512Test :
    BehaviorSpec({

        Given("The test vectors") {
            When("each provided input is used to derive keys") {
                Then("it should match the expected output") {
                    @Suppress("MaxLineLength")
                    forAll(
                        // modified from https://stackoverflow.com/a/19898265/178433
                        row(
                            "passDATAb00AB7YxDTT",
                            "saltKEYbcTcXHCBxtjD",
                            1,
                            512,
                            "cbe6088ad4359af42e603c2a33760ef9d4017a7b2aad10af46f992c660a0b461ecb0dc2a79c2570941bea6a08d15d6887e79f32b132e1c134e9525eeddd744fa"
                        ),
                        row(
                            "passDATAb00AB7YxDTTl",
                            "saltKEYbcTcXHCBxtjD2",
                            3,
                            512,
                            "3660a4d16e9f8c2d467a051d95444d33148fcb8e595767f05f554487a1f97426b8dad9a83538144539b14b9274a819a8bbe59267cc51073746eef67b6042ed9d"
                        ),
                        row(
                            "passDATAb00AB7YxDTTlR",
                            "saltKEYbcTcXHCBxtjD2P",
                            5,
                            512,
                            "cc2fe2ab0ba48720dc1db53e850219fca6c5eada37023cb952e7f26d4ab707bcf7e25360e28db6cc97df1d6bf5fa49b2e0b1282fd6b05fed5766dca7bb306a2c"
                        ),
                        row(
                            "passDATAb00AB7YxDTTlRH2dqxDx19GDxDV1zFMz7E6QVqKIzwOtMnlxQLttpE5",
                            "saltKEYbcTcXHCBxtjD2PnBh44AIQ6XUOCESOhXpEp3HrcGMwbjzQKMSaf63IJe",
                            7,
                            512,
                            "3cba5dc465030d7d883df2d2eba356c2daf047605d873be576385acf50d57a574d4ccc2f65cfd63d04c6746d553605c7a1eafbadb86fde0600c6a0fefab076c3"
                        ),
                        row(
                            "passDATAb00AB7YxDTTlRH2dqxDx19GDxDV1zFMz7E6QVqKIzwOtMnlxQLttpE57",
                            "saltKEYbcTcXHCBxtjD2PnBh44AIQ6XUOCESOhXpEp3HrcGMwbjzQKMSaf63IJem",
                            11,
                            512,
                            "22110678bafc2672df890e9e54f4a8ca4cd4b92894a36003f293de1209497e8c4b1ab7a0e5da5868e1398787a3d3dd7a54d3ef0912bcf2322dd0521cd342c156"
                        ),
                        row(
                            "passDATAb00AB7YxDTTlRH2dqxDx19GDxDV1zFMz7E6QVqKIzwOtMnlxQLttpE57U",
                            "saltKEYbcTcXHCBxtjD2PnBh44AIQ6XUOCESOhXpEp3HrcGMwbjzQKMSaf63IJemk",
                            13,
                            512,
                            "726a143f2843b01b3074351842db496f102ca333e3eef51ae262984812cc133e57d61c89d90f455d64555b38f7a8f5dbf74f2ab1f5e3bd30eda32103d76365cc"
                        ),
                        row(
                            "passDATAb00AB7YxDTTlRH2dqxDx19GDxDV1zFMz7E6QVqKIzwOtMnlxQLttpE57Un4u12D2YD7oOPpiEvCDYvntXEe4NNPLCnGGeJArbYDEu6xDoCfWH6kbuV6awi0",
                            "saltKEYbcTcXHCBxtjD2PnBh44AIQ6XUOCESOhXpEp3HrcGMwbjzQKMSaf63IJemkURWoqHusIeVB8Il91NjiCGQacPUu9qTFaShLbKG0Yj4RCMV56WPj7E14EMpbxy",
                            17,
                            512,
                            "b14ac88b22ca14b4a036159b9d671e542702ec07239108ea756040a7e189c6d4680e7875fc92849d853c93e9a89bf232a08fbd0d6e770a5b78ff6be5fd272d64"
                        ),
                        row(
                            "passDATAb00AB7YxDTTlRH2dqxDx19GDxDV1zFMz7E6QVqKIzwOtMnlxQLttpE57Un4u12D2YD7oOPpiEvCDYvntXEe4NNPLCnGGeJArbYDEu6xDoCfWH6kbuV6awi04",
                            "saltKEYbcTcXHCBxtjD2PnBh44AIQ6XUOCESOhXpEp3HrcGMwbjzQKMSaf63IJemkURWoqHusIeVB8Il91NjiCGQacPUu9qTFaShLbKG0Yj4RCMV56WPj7E14EMpbxy6",
                            19,
                            512,
                            "9f6b95569376f14c9b716741297b0f64e9ea46b837c7a4c2831d3c9f7a94633aa4eda057916ae03c09030aed2c6dd6203172e257ab08b98aa1ce60fe90a7a18f"
                        ),
                        row(
                            "passDATAb00AB7YxDTTlRH2dqxDx19GDxDV1zFMz7E6QVqKIzwOtMnlxQLttpE57Un4u12D2YD7oOPpiEvCDYvntXEe4NNPLCnGGeJArbYDEu6xDoCfWH6kbuV6awi04U",
                            "saltKEYbcTcXHCBxtjD2PnBh44AIQ6XUOCESOhXpEp3HrcGMwbjzQKMSaf63IJemkURWoqHusIeVB8Il91NjiCGQacPUu9qTFaShLbKG0Yj4RCMV56WPj7E14EMpbxy6P",
                            23,
                            512,
                            "92c92d9ffe4242ed0691a8b704834621e909e2a03c101b4f90e098909039b819f3bcb55f08058fa96412d17c2e2b0ad52b095f782fb2969c50ff1e1262844cf9"
                        ),
                        row(
                            "passDATAb00AB7YxDTTlRH2dqxDx19GDxDV1zFMz7E6QVqKIzwOtMnlxQLttpE57Un4u12D2YD7oOPpiEvCDYvntXEe4NNPLCnGGeJArbYDEu6xDoCfWH6kbuV6awi04Uz3ebEAhzZ4ve1A2wg5CnLXdZC5Y7gwfVgbEgZSTmoYQSzC5OW4dfrjqiwApTACO6xoOL1AjWj6X6f6qFfF8TVmOzU9RhOd1N4QtzWI4fP6FYttNz5FuLdtYVXWVXH2Tf7I9fieMeWCHTMkM4VcmQyQHpbcP8MEb5f1g6Ckg5xk3HQr3wMBvQcOHpCPy1K8HCM7a5wkPDhgVA0BVmwNpsRIbDQZRtHK6dT6bGyalp6gbFZBuBHwD86gTzkrFY7HkOVrgc0gJcGJZe65Ce8v4Jn5OzkuVsiU8efm2Pw2RnbpWSAr7SkVdCwXK2XSJDQ5fZ4HBEz9VTFYrG23ELuLjvx5njOLNgDAJuf5JB2tn4nMjjcnl1e8qcYVwZqFzEv2zhLyDWMkV4tzl4asLnvyAxTBkxPRZj2pRABWwb3kEofpsHYxMTAn38YSpZreoXipZWBnu6HDURaruXaIPYFPYHl9Ls9wsuD7rzaGfbOyfVgLIGK5rODphwRA7lm88bGKY8b7tWOtepyEvaLxMI7GZF5ScwpZTYeEDNUKPzvM2Im9zehIaznpguNdNXNMLWnwPu4H6zEvajkw3G3ucSiXKmh6XNe3hkdSANm3vnxzRXm4fcuzAx68IElXE2bkGFElluDLo6EsUDWZ4JIWBVaDwYdJx8uCXbQdoifzCs5kuuClaDaDqIhb5hJ2WR8mxiueFsS0aDGdIYmye5svmNmzQxFmdOkHoF7CfwuU1yy4uEEt9vPSP2wFp1dyaMvJW68vtB4kddLmI6gIgVVcT6ZX1Qm6WsusPrdisPLB2ScodXojCbL3DLj6PKG8QDVMWTrL1TpafT2wslRledWIhsTlv2mI3C066WMcTSwKLXdEDhVvFJ6ShiLKSN7gnRrlE0BnAw",
                            "saltKEYbcTcXHCBxtjD2PnBh44AIQ6XUOCESOhXpEp3HrcGMwbjzQKMSaf63IJemkURWoqHusIeVB8Il91NjiCGQacPUu9qTFaShLbKG0Yj4RCMV56WPj7E14EMpbxy6PlBdILBOkKUB6TGTPJXh1tpdOHTG6KuIvcbQp9qWjaf1uxAKgiTtYRIHhxjJI2viVa6fDZ67QOouOaf2RXQhpsWaTtAVnff6PIFcvJhdPDFGV5nvmZWoCZQodj6yXRDHPw9PyF0iLYm9uFtEunlAAxGB5qqea4X5tZvB1OfLVwymY3a3JPjdxTdvHxCHbqqE0zip61JNqdmeWxGtlRBC6CGoCiHO4XxHCntQBRJDcG0zW7joTdgtTBarsQQhlLXBGMNBSNmmTbDf3hFtawUBCJH18IAiRMwyeQJbJ2bERsY3MVRPuYCf4Au7gN72iGh1lRktSQtEFye7pO46kMXRrEjHQWXInMzzy7X2StXUzHVTFF2VdOoKn0WUqFNvB6PF7qIsOlYKj57bi1Psa34s85WxMSbTkhrd7VHdHZkTVaWdraohXYOePdeEvIwObCGEXkETUzqM5P2yzoBOJSdjpIYaa8zzdLD3yrb1TwCZuJVxsrq0XXY6vErU4QntsW0972XmGNyumFNJiPm4ONKh1RLvS1kddY3nm8276S4TUuZfrRQO8QxZRNuSaZI8JRZp5VojB5DktuMxAQkqoPjQ5Vtb6oXeOyY591CB1MEW1fLTCs0NrL321SaNRMqza1ETogAxpEiYwZ6pIgnMmSqNMRdZnCqA4gMWw1lIVATWK83OCeicNRUNOdfzS7A8vbLcmvKPtpOFvhNzwrrUdkvuKvaYJviQgeR7snGetO9JLCwIlHIj52gMCNU18d32SJl7Xomtl3wIe02SMvq1i1BcaX7lXioqWGmgVqBWU3fsUuGwHi6RUKCCQdEOBfNo2WdpFaCflcgnn0O6jVHCqkv8cQk81AqS00rAmHGCNTwyA6Tq5TXoLlDnC8gAQjDUsZp0z",
                            29,
                            512,
                            "5edc3d6649fa05c07622dede976997afe683f8b489d996509e2bf9421cd81f49b7bd38e78ad7ccad0a2a9070710ad451da7b6f5b207a0ee17c14ad2054bf492a"
                        ),
                        row(
                            "passDATAb00AB7YxDTT",
                            "saltKEYbcTcXHCBxtjD",
                            31,
                            504,
                            "15530800da88a0776a812937eb2afeea4a2e7ecad633a918f1024688f73c5721d8bfcaa87f253cf50b9181ab3bb28043e13b1ce859f71d002674806bab0547"
                        ),
                        row(
                            "passDATAb00AB7YxDTTl",
                            "saltKEYbcTcXHCBxtjD2",
                            37,
                            504,
                            "6a71211a3b59e4b76fe962e17c2db6232a84a10edc043807831992665ff9d0b9cc76c6f5dc84297050bdd026e05144e3e651b3f8b4108bb050e576ba0b9440"
                        ),
                        row(
                            "passDATAb00AB7YxDTTlR",
                            "saltKEYbcTcXHCBxtjD2P",
                            41,
                            504,
                            "613a19696be76eb92a705b9a2fe6eb12cab31086c9b2778b8b83fc7f40cc3a02b39b3b17cbd0c97938be2e6e8d6f6bf73afbe7dea8cffcdd4e4bc6853f4e40"
                        ),
                        row(
                            "passDATAb00AB7YxDTTlRH2dqxDx19GDxDV1zFMz7E6QVqKIzwOtMnlxQLttpE5",
                            "saltKEYbcTcXHCBxtjD2PnBh44AIQ6XUOCESOhXpEp3HrcGMwbjzQKMSaf63IJe",
                            43,
                            504,
                            "e0d5567fb5f45381284d7d67a3386f943e14b1af2766b675cdd988614e40ffc012b9f320b5e33d3f6aea8af3fcd2f1077b4082e70414750af2b1b3bbf5948b"
                        ),
                        row(
                            "passDATAb00AB7YxDTTlRH2dqxDx19GDxDV1zFMz7E6QVqKIzwOtMnlxQLttpE57",
                            "saltKEYbcTcXHCBxtjD2PnBh44AIQ6XUOCESOhXpEp3HrcGMwbjzQKMSaf63IJem",
                            47,
                            504,
                            "c92ce4dceb97d3411e7a6ccd21d143d6a0f830b3d6e2ebe8dd6ff5aaac8d879bb7cda02dd652471243cb30e3bef8213dc4b6e4e9e2623af9702d67c30ea8ed"
                        ),
                        row(
                            "passDATAb00AB7YxDTTlRH2dqxDx19GDxDV1zFMz7E6QVqKIzwOtMnlxQLttpE57U",
                            "saltKEYbcTcXHCBxtjD2PnBh44AIQ6XUOCESOhXpEp3HrcGMwbjzQKMSaf63IJemk",
                            53,
                            504,
                            "40de5197dc99cf3c788ba10ad93fe8213584cc2c9304fce1ad2c0df261e28aac6769bebf7b19f4ca5b9758ad97c193a31a89faa80f1a0e75c8347da134f4c6"
                        ),
                        row(
                            "passDATAb00AB7YxDTTlRH2dqxDx19GDxDV1zFMz7E6QVqKIzwOtMnlxQLttpE57Un4u12D2YD7oOPpiEvCDYvntXEe4NNPLCnGGeJArbYDEu6xDoCfWH6kbuV6awi0",
                            "saltKEYbcTcXHCBxtjD2PnBh44AIQ6XUOCESOhXpEp3HrcGMwbjzQKMSaf63IJemkURWoqHusIeVB8Il91NjiCGQacPUu9qTFaShLbKG0Yj4RCMV56WPj7E14EMpbxy",
                            61,
                            504,
                            "d2e627cbe497af8eba3c2d22bbb765c14b065259d3147f58e2d567236aebd0c9a2b00f4cf2d54f3f8235ee02d3541d4c6f7240f5fd9a47a94ce914a648c016"
                        ),
                        row(
                            "passDATAb00AB7YxDTTlRH2dqxDx19GDxDV1zFMz7E6QVqKIzwOtMnlxQLttpE57Un4u12D2YD7oOPpiEvCDYvntXEe4NNPLCnGGeJArbYDEu6xDoCfWH6kbuV6awi04",
                            "saltKEYbcTcXHCBxtjD2PnBh44AIQ6XUOCESOhXpEp3HrcGMwbjzQKMSaf63IJemkURWoqHusIeVB8Il91NjiCGQacPUu9qTFaShLbKG0Yj4RCMV56WPj7E14EMpbxy6",
                            67,
                            504,
                            "cc51a623e2a197a8b009815a6d4ebebaaa247beb7a0643ea2b77909ff10b4c6d85bf080e55f2954ae003a1cfb78fd940fedfd67fe8ed4849e2bc1ae42ec055"
                        ),
                        row(
                            "passDATAb00AB7YxDTTlRH2dqxDx19GDxDV1zFMz7E6QVqKIzwOtMnlxQLttpE57Un4u12D2YD7oOPpiEvCDYvntXEe4NNPLCnGGeJArbYDEu6xDoCfWH6kbuV6awi04U",
                            "saltKEYbcTcXHCBxtjD2PnBh44AIQ6XUOCESOhXpEp3HrcGMwbjzQKMSaf63IJemkURWoqHusIeVB8Il91NjiCGQacPUu9qTFaShLbKG0Yj4RCMV56WPj7E14EMpbxy6P",
                            71,
                            504,
                            "f8e55259f1d1d884d0ffbd1d22bc7894c53e2a313131499ba8f35faeda6208a80c16ac93f5c0b79c3a1575d92f6a833ae7036d52be926e6468637c28222cd7"
                        ),
                        row(
                            "passDATAb00AB7YxDTTlRH2dqxDx19GDxDV1zFMz7E6QVqKIzwOtMnlxQLttpE57Un4u12D2YD7oOPpiEvCDYvntXEe4NNPLCnGGeJArbYDEu6xDoCfWH6kbuV6awi04Uz3ebEAhzZ4ve1A2wg5CnLXdZC5Y7gwfVgbEgZSTmoYQSzC5OW4dfrjqiwApTACO6xoOL1AjWj6X6f6qFfF8TVmOzU9RhOd1N4QtzWI4fP6FYttNz5FuLdtYVXWVXH2Tf7I9fieMeWCHTMkM4VcmQyQHpbcP8MEb5f1g6Ckg5xk3HQr3wMBvQcOHpCPy1K8HCM7a5wkPDhgVA0BVmwNpsRIbDQZRtHK6dT6bGyalp6gbFZBuBHwD86gTzkrFY7HkOVrgc0gJcGJZe65Ce8v4Jn5OzkuVsiU8efm2Pw2RnbpWSAr7SkVdCwXK2XSJDQ5fZ4HBEz9VTFYrG23ELuLjvx5njOLNgDAJuf5JB2tn4nMjjcnl1e8qcYVwZqFzEv2zhLyDWMkV4tzl4asLnvyAxTBkxPRZj2pRABWwb3kEofpsHYxMTAn38YSpZreoXipZWBnu6HDURaruXaIPYFPYHl9Ls9wsuD7rzaGfbOyfVgLIGK5rODphwRA7lm88bGKY8b7tWOtepyEvaLxMI7GZF5ScwpZTYeEDNUKPzvM2Im9zehIaznpguNdNXNMLWnwPu4H6zEvajkw3G3ucSiXKmh6XNe3hkdSANm3vnxzRXm4fcuzAx68IElXE2bkGFElluDLo6EsUDWZ4JIWBVaDwYdJx8uCXbQdoifzCs5kuuClaDaDqIhb5hJ2WR8mxiueFsS0aDGdIYmye5svmNmzQxFmdOkHoF7CfwuU1yy4uEEt9vPSP2wFp1dyaMvJW68vtB4kddLmI6gIgVVcT6ZX1Qm6WsusPrdisPLB2ScodXojCbL3DLj6PKG8QDVMWTrL1TpafT2wslRledWIhsTlv2mI3C066WMcTSwKLXdEDhVvFJ6ShiLKSN7gnRrlE0BnAw",
                            "saltKEYbcTcXHCBxtjD2PnBh44AIQ6XUOCESOhXpEp3HrcGMwbjzQKMSaf63IJemkURWoqHusIeVB8Il91NjiCGQacPUu9qTFaShLbKG0Yj4RCMV56WPj7E14EMpbxy6PlBdILBOkKUB6TGTPJXh1tpdOHTG6KuIvcbQp9qWjaf1uxAKgiTtYRIHhxjJI2viVa6fDZ67QOouOaf2RXQhpsWaTtAVnff6PIFcvJhdPDFGV5nvmZWoCZQodj6yXRDHPw9PyF0iLYm9uFtEunlAAxGB5qqea4X5tZvB1OfLVwymY3a3JPjdxTdvHxCHbqqE0zip61JNqdmeWxGtlRBC6CGoCiHO4XxHCntQBRJDcG0zW7joTdgtTBarsQQhlLXBGMNBSNmmTbDf3hFtawUBCJH18IAiRMwyeQJbJ2bERsY3MVRPuYCf4Au7gN72iGh1lRktSQtEFye7pO46kMXRrEjHQWXInMzzy7X2StXUzHVTFF2VdOoKn0WUqFNvB6PF7qIsOlYKj57bi1Psa34s85WxMSbTkhrd7VHdHZkTVaWdraohXYOePdeEvIwObCGEXkETUzqM5P2yzoBOJSdjpIYaa8zzdLD3yrb1TwCZuJVxsrq0XXY6vErU4QntsW0972XmGNyumFNJiPm4ONKh1RLvS1kddY3nm8276S4TUuZfrRQO8QxZRNuSaZI8JRZp5VojB5DktuMxAQkqoPjQ5Vtb6oXeOyY591CB1MEW1fLTCs0NrL321SaNRMqza1ETogAxpEiYwZ6pIgnMmSqNMRdZnCqA4gMWw1lIVATWK83OCeicNRUNOdfzS7A8vbLcmvKPtpOFvhNzwrrUdkvuKvaYJviQgeR7snGetO9JLCwIlHIj52gMCNU18d32SJl7Xomtl3wIe02SMvq1i1BcaX7lXioqWGmgVqBWU3fsUuGwHi6RUKCCQdEOBfNo2WdpFaCflcgnn0O6jVHCqkv8cQk81AqS00rAmHGCNTwyA6Tq5TXoLlDnC8gAQjDUsZp0z",
                            73,
                            504,
                            "d5ef8859566cabceb37b6f4a91e54a36067084bed91d9ccbb4d1e65942764cc5ff45304a4788f1a181e4415df2104f299aaadaed25392b74d5ecdf1af09c10"
                        ),
                        row(
                            "passDATAb00AB7YxDTT",
                            "saltKEYbcTcXHCBxtjD",
                            79,
                            520,
                            "3b9359b5639de3f8ec4009491b5fafe764548794c87f44a9fd6a7b9364522bee36b6b71819b71e9130dc6df1db6eba29133393762d9d89f68dd2d5d9d61488937e"
                        ),
                        row(
                            "passDATAb00AB7YxDTTl",
                            "saltKEYbcTcXHCBxtjD2",
                            83,
                            520,
                            "8ec068d1a5ad8aec6ea95aab0b4545e86adeb940bfa71c9b6e8969cd70239ec60020137c8094cf466d2129f98bc55b53077e0befb72615f0fe38554cb22f2cf455"
                        ),
                        row(
                            "passDATAb00AB7YxDTTlR",
                            "saltKEYbcTcXHCBxtjD2P",
                            89,
                            520,
                            "c882fda77fec48a78af3393a27cdcda40f8392ac5997ed150e45cc501dbcb1a4fdb770556f12f6c7c22bd8d111051bb6a9a260cff821f2cb5902ea6a6536338cf9"
                        ),
                        row(
                            "passDATAb00AB7YxDTTlRH2dqxDx19GDxDV1zFMz7E6QVqKIzwOtMnlxQLttpE5",
                            "saltKEYbcTcXHCBxtjD2PnBh44AIQ6XUOCESOhXpEp3HrcGMwbjzQKMSaf63IJe",
                            97,
                            520,
                            "d641b7852c0f5ad0bba73b155722bd9f0a6142765d0719eaabc36d25d7dc0a10edb2511d463748e611349a6c71b9b7f5dea5e445c1c98afc209387d6786970d21c"
                        ),
                        row(
                            "passDATAb00AB7YxDTTlRH2dqxDx19GDxDV1zFMz7E6QVqKIzwOtMnlxQLttpE57",
                            "saltKEYbcTcXHCBxtjD2PnBh44AIQ6XUOCESOhXpEp3HrcGMwbjzQKMSaf63IJem",
                            101,
                            520,
                            "d44e4dae8a7a5223898f419c18645191d57748bfdeef5b0f49bc36b5efa3611c76e82f097de834de3294bbad9f0a8071c0a09587bf748ed04118706f384ee87679"
                        ),
                        row(
                            "passDATAb00AB7YxDTTlRH2dqxDx19GDxDV1zFMz7E6QVqKIzwOtMnlxQLttpE57U",
                            "saltKEYbcTcXHCBxtjD2PnBh44AIQ6XUOCESOhXpEp3HrcGMwbjzQKMSaf63IJemk",
                            103,
                            520,
                            "2dbd66db3e0b9de948e5a76821a246bb1b03d5da68d1c1c0f7c6dbc41d5716caff82ac844d8107febb96ae9bae3958ade57528e27e53dab024263701ea432b9ed4"
                        ),
                        row(
                            "passDATAb00AB7YxDTTlRH2dqxDx19GDxDV1zFMz7E6QVqKIzwOtMnlxQLttpE57Un4u12D2YD7oOPpiEvCDYvntXEe4NNPLCnGGeJArbYDEu6xDoCfWH6kbuV6awi0",
                            "saltKEYbcTcXHCBxtjD2PnBh44AIQ6XUOCESOhXpEp3HrcGMwbjzQKMSaf63IJemkURWoqHusIeVB8Il91NjiCGQacPUu9qTFaShLbKG0Yj4RCMV56WPj7E14EMpbxy",
                            107,
                            520,
                            "2186bb78640371f3912d8ce507a4323c903608ec54b85ecc43608eafe52ee1f403f3e68e09a150599ca9f70097bc51f232d6449586a70fc5b7b0a21c629110563d"
                        ),
                        row(
                            "passDATAb00AB7YxDTTlRH2dqxDx19GDxDV1zFMz7E6QVqKIzwOtMnlxQLttpE57Un4u12D2YD7oOPpiEvCDYvntXEe4NNPLCnGGeJArbYDEu6xDoCfWH6kbuV6awi04",
                            "saltKEYbcTcXHCBxtjD2PnBh44AIQ6XUOCESOhXpEp3HrcGMwbjzQKMSaf63IJemkURWoqHusIeVB8Il91NjiCGQacPUu9qTFaShLbKG0Yj4RCMV56WPj7E14EMpbxy6",
                            109,
                            520,
                            "c512aa138dd99b1a785c3ec048dddd4eb569d7f9eb7f206b0544746e266f214fdcc4f5d5ba0869140010bdce517a550c58b527439dc40463f9c7fba7e2cbbc8820"
                        ),
                        row(
                            "passDATAb00AB7YxDTTlRH2dqxDx19GDxDV1zFMz7E6QVqKIzwOtMnlxQLttpE57Un4u12D2YD7oOPpiEvCDYvntXEe4NNPLCnGGeJArbYDEu6xDoCfWH6kbuV6awi04U",
                            "saltKEYbcTcXHCBxtjD2PnBh44AIQ6XUOCESOhXpEp3HrcGMwbjzQKMSaf63IJemkURWoqHusIeVB8Il91NjiCGQacPUu9qTFaShLbKG0Yj4RCMV56WPj7E14EMpbxy6P",
                            113,
                            520,
                            "e4c2be8f5cad779f90f54bec52888d6a1684f55d5145103515981217cc6609a039a86a41b3d22bae22f9a6687a605ae5c9e9dc411d83ba892f69af608b37fb89e8"
                        ),
                        row(
                            "passDATAb00AB7YxDTTlRH2dqxDx19GDxDV1zFMz7E6QVqKIzwOtMnlxQLttpE57Un4u12D2YD7oOPpiEvCDYvntXEe4NNPLCnGGeJArbYDEu6xDoCfWH6kbuV6awi04Uz3ebEAhzZ4ve1A2wg5CnLXdZC5Y7gwfVgbEgZSTmoYQSzC5OW4dfrjqiwApTACO6xoOL1AjWj6X6f6qFfF8TVmOzU9RhOd1N4QtzWI4fP6FYttNz5FuLdtYVXWVXH2Tf7I9fieMeWCHTMkM4VcmQyQHpbcP8MEb5f1g6Ckg5xk3HQr3wMBvQcOHpCPy1K8HCM7a5wkPDhgVA0BVmwNpsRIbDQZRtHK6dT6bGyalp6gbFZBuBHwD86gTzkrFY7HkOVrgc0gJcGJZe65Ce8v4Jn5OzkuVsiU8efm2Pw2RnbpWSAr7SkVdCwXK2XSJDQ5fZ4HBEz9VTFYrG23ELuLjvx5njOLNgDAJuf5JB2tn4nMjjcnl1e8qcYVwZqFzEv2zhLyDWMkV4tzl4asLnvyAxTBkxPRZj2pRABWwb3kEofpsHYxMTAn38YSpZreoXipZWBnu6HDURaruXaIPYFPYHl9Ls9wsuD7rzaGfbOyfVgLIGK5rODphwRA7lm88bGKY8b7tWOtepyEvaLxMI7GZF5ScwpZTYeEDNUKPzvM2Im9zehIaznpguNdNXNMLWnwPu4H6zEvajkw3G3ucSiXKmh6XNe3hkdSANm3vnxzRXm4fcuzAx68IElXE2bkGFElluDLo6EsUDWZ4JIWBVaDwYdJx8uCXbQdoifzCs5kuuClaDaDqIhb5hJ2WR8mxiueFsS0aDGdIYmye5svmNmzQxFmdOkHoF7CfwuU1yy4uEEt9vPSP2wFp1dyaMvJW68vtB4kddLmI6gIgVVcT6ZX1Qm6WsusPrdisPLB2ScodXojCbL3DLj6PKG8QDVMWTrL1TpafT2wslRledWIhsTlv2mI3C066WMcTSwKLXdEDhVvFJ6ShiLKSN7gnRrlE0BnAw",
                            "saltKEYbcTcXHCBxtjD2PnBh44AIQ6XUOCESOhXpEp3HrcGMwbjzQKMSaf63IJemkURWoqHusIeVB8Il91NjiCGQacPUu9qTFaShLbKG0Yj4RCMV56WPj7E14EMpbxy6PlBdILBOkKUB6TGTPJXh1tpdOHTG6KuIvcbQp9qWjaf1uxAKgiTtYRIHhxjJI2viVa6fDZ67QOouOaf2RXQhpsWaTtAVnff6PIFcvJhdPDFGV5nvmZWoCZQodj6yXRDHPw9PyF0iLYm9uFtEunlAAxGB5qqea4X5tZvB1OfLVwymY3a3JPjdxTdvHxCHbqqE0zip61JNqdmeWxGtlRBC6CGoCiHO4XxHCntQBRJDcG0zW7joTdgtTBarsQQhlLXBGMNBSNmmTbDf3hFtawUBCJH18IAiRMwyeQJbJ2bERsY3MVRPuYCf4Au7gN72iGh1lRktSQtEFye7pO46kMXRrEjHQWXInMzzy7X2StXUzHVTFF2VdOoKn0WUqFNvB6PF7qIsOlYKj57bi1Psa34s85WxMSbTkhrd7VHdHZkTVaWdraohXYOePdeEvIwObCGEXkETUzqM5P2yzoBOJSdjpIYaa8zzdLD3yrb1TwCZuJVxsrq0XXY6vErU4QntsW0972XmGNyumFNJiPm4ONKh1RLvS1kddY3nm8276S4TUuZfrRQO8QxZRNuSaZI8JRZp5VojB5DktuMxAQkqoPjQ5Vtb6oXeOyY591CB1MEW1fLTCs0NrL321SaNRMqza1ETogAxpEiYwZ6pIgnMmSqNMRdZnCqA4gMWw1lIVATWK83OCeicNRUNOdfzS7A8vbLcmvKPtpOFvhNzwrrUdkvuKvaYJviQgeR7snGetO9JLCwIlHIj52gMCNU18d32SJl7Xomtl3wIe02SMvq1i1BcaX7lXioqWGmgVqBWU3fsUuGwHi6RUKCCQdEOBfNo2WdpFaCflcgnn0O6jVHCqkv8cQk81AqS00rAmHGCNTwyA6Tq5TXoLlDnC8gAQjDUsZp0z",
                            127,
                            520,
                            "bb344a5712d07c4c49dfb9f77e44c5b4c29406c78c84214b07defb36a7898ae7a96c6cfeaf8d753b4bde382c4e48f247a90c17df79726228e2fed11c40b98e2648"
                        )
                    ) { password: String, salt: String, count: Int, length: Int, expected: String ->
                        val result = Pbkdf2Sha512.derive(password.toCharArray(), salt.encodeToByteArray(), count, length)
                        result.toHex() shouldBe expected
                    }
                }
            }
        }
    })
