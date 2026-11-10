package tw.edu.pu.csim.zlun.canvas

import android.graphics.Paint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
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

    // 控制動畫是否播放的變數
    var isPlaying by remember { mutableStateOf(true) }

    // 紀錄小小兵目前的 X 與 Y 座標
    var posX by remember { mutableFloatStateOf(0f) }
    var posY by remember { mutableFloatStateOf(0f) }

    // 速度變數
    val velocityX = 100f
    val velocityY = 100f

    // 當 isPlaying 為 true 時，每隔 50ms 更新位置
    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            delay(50) // 每 50 毫秒 (0.05秒) 更新一次
            val dt = 0.05f

            posX += velocityX * dt
            posY += velocityY * dt
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val imageWidth = minionsImage.width.toFloat()
            val imageHeight = minionsImage.height.toFloat()

            // ==========================================
            // 修改處：當圖片完全跑出畫布右側或下方時，回到原點 (0, 0)
            // ==========================================
            if (posX > canvasWidth || posY > canvasHeight) {
                posX = 0f
                posY = 0f
            }

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
                style = Stroke(width = 5f)
            )

            // 繪製紅色的線，從 (200,800) 畫到右下角，線條粗細 10
            drawLine(
                Color.Red,
                Offset(200f, 800f),
                Offset(canvasWidth, canvasHeight),
                strokeWidth = 10f
            )

            // 將圖片繪製在即時更新的座標上
            translate(posX, posY) {
                drawImage(minionsImage)
            }

            // 顯示畫布寬度與高度
            val resolutionText = "畫布寬度：${canvasWidth.toInt()} , 畫布高度：${canvasHeight.toInt()}"
            drawIntoCanvas { canvas ->
                val paint = Paint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = 40f
                    isAntiAlias = true
                }
                canvas.nativeCanvas.drawText(resolutionText, 0f, 50f, paint)
            }
        }

        // 暫停 / 開始 按鈕
        Button(
            onClick = { isPlaying = !isPlaying },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(32.dp)
        ) {
            Text(text = if (isPlaying) "暫停 (Pause)" else "開始 (Start)")
        }
    }
}