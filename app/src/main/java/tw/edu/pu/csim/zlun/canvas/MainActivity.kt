package tw.edu.pu.csim.zlun.canvas

import android.graphics.Paint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.imageResource
import tw.edu.pu.csim.zlun.canvas.ui.theme.CanvasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CanvasTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TestCanvas(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun TestCanvas(modifier: Modifier = Modifier) {
    // 載入圖片資源 (小兵)
    val minionsImage: ImageBitmap =
        ImageBitmap.imageResource(id = R.drawable.minions)

    Canvas(
        modifier = modifier.fillMaxSize()
    ) {
        // 繪製洋紅色的圓, 半徑 100, 圓心 (250,700)
        drawCircle(
            Color.Magenta,
            100f,
            Offset(250f, 700f)
        )

        // 繪製藍色空心矩形, 左上角 (300,200), 寬度與高度 200
        drawRect(
            Color.Blue,
            Offset(300f, 200f),
            Size(200f, 200f),
            style = Stroke(width = 5f) // 使用 style 控制外框樣式
        )

        // 繪製紅色的線，從 (200,800) 畫到右下角，線條粗細 10
        drawLine(
            Color.Red,
            Offset(200f, 800f),
            Offset(size.width, size.height),
            strokeWidth = 10f
        )

        // ==========================================
        // 將圖片放在左上角 (offsetX = 0f, offsetY = 0f)
        // ==========================================
        val offsetX = 0f
        val offsetY = 0f

        // 將畫布起點移至左上角後畫圖
        translate(offsetX, offsetY) {
            drawImage(minionsImage)
        }

        // 顯示畫布寬度與高度
        val resolutionText = "畫布寬度：${size.width.toInt()} , 畫布高度：${size.height.toInt()}"
        drawIntoCanvas { canvas ->
            val paint = Paint().apply {
                color = android.graphics.Color.BLACK
                textSize = 40f
                isAntiAlias = true
            }
            // 在座標 (50, 150) 的位置繪製文字
            canvas.nativeCanvas.drawText(resolutionText, 0f, 0f, paint)
        }
    }
}