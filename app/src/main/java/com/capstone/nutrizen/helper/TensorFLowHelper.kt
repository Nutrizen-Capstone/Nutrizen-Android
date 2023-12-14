package com.capstone.nutrizen.helper

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.capstone.nutrizen.ml.MobileNet
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

object TensorFLowHelper {

    val imageSize = 299

    @Composable
    fun classifyImage(image: Bitmap, callback: (@Composable (fruit: String) -> Unit)) {
        val model: MobileNet = MobileNet.newInstance(LocalContext.current)

        // Creates inputs for reference.
        val inputFeature0 =
            TensorBuffer.createFixedSize(intArrayOf(1, 299, 299, 3), DataType.FLOAT32)
        val byteBuffer: ByteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3)
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(imageSize * imageSize)
        image.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)
        var pixel = 0
        //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
        for (i in 0 until imageSize) {
            for (j in 0 until imageSize) {
                val `val` = intValues[pixel++] // RGB
                byteBuffer.putFloat((`val` shr 16 and 0xFF) * (1f / 1))
                byteBuffer.putFloat((`val` shr 8 and 0xFF) * (1f / 1))
                byteBuffer.putFloat((`val` and 0xFF) * (1f / 1))
            }
        }
        inputFeature0.loadBuffer(byteBuffer)

        // Runs model inference and gets result.
        val outputs: MobileNet.Outputs = model.process(inputFeature0)
        val outputFeature0: TensorBuffer = outputs.getOutputFeature0AsTensorBuffer()
        val confidences = outputFeature0.floatArray
        // find the index of the class with the biggest confidence.
        var maxPos = 0
        var maxConfidence = 0f
        for (i in confidences.indices) {
            if (confidences[i] > maxConfidence) {
                maxConfidence = confidences[i]
                maxPos = i
            }
        }
        val classes = arrayOf(
            "ayam bakar",
            "ayam goreng",
            "bakso",
            "bakwan",
            "batagor",
            "bihun",
            "bika_ambon",
            "capcay",
            "dadar_gulung",
            "gado-gado",
            "ikan goreng",
            "kerupuk",
            "martabak telur",
            "mie",
            "nasi goreng",
            "nasi putih",
            "nugget",
            "opor ayam",
            "pempek",
            "rendang",
            "roti",
            "sate",
            "sosis",
            "soto",
            "steak",
            "tahu",
            "telur",
            "tempe",
            "terong balado",
            "tumis kangkung",
            "udang"
        )
        callback.invoke(classes[maxPos])

        // Releases model resources if no longer used.
        model.close()

    }

}