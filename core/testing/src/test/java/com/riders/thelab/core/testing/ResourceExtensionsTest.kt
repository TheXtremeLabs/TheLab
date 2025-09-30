package com.riders.thelab.core.testing

import com.riders.thelab.core.testing.utils.getResourceAsStringData
import com.riders.thelab.core.testing.utils.getResourceFileAsInputStream
import com.riders.thelab.core.testing.utils.log
import org.junit.Before
import org.junit.Test
import java.io.InputStream
import kotlin.test.assertTrue

class ResourceExtensionsTest {

    var testFileStream: InputStream? = null
    var testFileString: String? = null

    @Before
    fun setup() {
        println("========================= BEGINNING OF TEST =========================")
    }

    @Test
    fun test_resource_extensions() {
        log(methodName = "test_resource_extensions", message = "")
        testFileStream = this
            .getResourceFileAsInputStream(TEST_FILENAME)
            ?.also {
                log("test_resource_extensions", "file size " + it.readAllBytes().size)
            }
        assertTrue { null != testFileStream }
    }

    @Test
    fun test_resource_extensions_as_string() {
        log(methodName = "test_resource_extensions", message = "")
        testFileString = this.getResourceAsStringData(TEST_FILENAME)?.also {
            log("test_resource_extensions_as_string", "file content : $it")
        }
        assertTrue { null != testFileString }
        assertTrue { testFileString!!.trim().isNotBlank() }
    }

    companion object {
        const val TEST_FILENAME = "test.txt"
    }
}