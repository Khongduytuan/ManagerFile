package com.eagletech.managerfile.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eagletech.managerfile.ui.theme.borderItemStorage
import com.eagletech.managerfile.ui.theme.textDesStorage
import com.eagletech.managerfile.ui.theme.textDetailStorage
import com.eagletech.managerfile.ui.theme.textHeaderScreenColor
import com.eagletech.managerfile.ui.theme.textSeeAll
import com.eagletech.managerfile.ui.theme.textTitleStorage
import kotlin.math.roundToInt

class HomeScreen {
}


@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

}

@Composable
fun ItemStorage(
    modifier: Modifier = Modifier,
    isUsed: Boolean = true,
    typeStorage: String,
    usedStorage: Int,
    totalStorage: Int,
    viewDetailClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .border(
                width = 2.dp,
                color = borderItemStorage,
                shape = RoundedCornerShape(12.dp)
            ) // borderItemStorage

    ) {
        Row(
            modifier = modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            AnimatedCircleProgress(
                modifier = modifier.padding(4.dp),
                totalStorage = 128f,
                usedStorage = 85f
            )
            Spacer(modifier = modifier.width(12.dp))
            Column(
                modifier = modifier.wrapContentSize(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = modifier,
                    text = typeStorage,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.W500,
                        color = textTitleStorage,
                        fontSize = 16.sp
                    ),
                )
                Text(
                    modifier = modifier,
                    text = "$usedStorage GB of $totalStorage used",
                    maxLines = 1,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.W700,
                        color = textDesStorage,
                        fontSize = 14.sp
                    ),
                )
                Text(
                    modifier = modifier.clickable {
                        viewDetailClick()
                    },
                    text = "View Details",
                    maxLines = 1,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.W400,
                        color = textDetailStorage,
                        fontSize = 12.sp
                    ),
                )
            }
        }

    }

}


@Composable
fun AnimatedCircleProgress(
    totalStorage: Float, // Tổng dung lượng (100%)
    usedStorage: Float,  // Dung lượng đã sử dụng
    modifier: Modifier = Modifier,
    animationDuration: Int = 1000 // Thời gian animation (ms)
) {
    // Tính phần trăm và góc quét tối đa
    val maxProgressPercentage = (usedStorage / totalStorage * 100).roundToInt()
    val maxSweepAngle = (usedStorage / totalStorage) * 360f

    // Tạo giá trị animatable cho góc quét và phần trăm
    val animatedSweepAngle = remember { Animatable(0f) }
    val animatedPercentage = remember { Animatable(0f) }

    // Chạy animation khi composable được vẽ
    LaunchedEffect(Unit) {
        // Animation cho góc quét
        animatedSweepAngle.animateTo(
            targetValue = maxSweepAngle,
            animationSpec = tween(
                durationMillis = animationDuration,
                easing = { fraction -> fraction } // Linear easing, bạn có thể thay đổi
            )
        )
    }

    LaunchedEffect(Unit) {
        // Animation cho phần trăm
        animatedPercentage.animateTo(
            targetValue = maxProgressPercentage.toFloat(),
            animationSpec = tween(
                durationMillis = animationDuration,
                easing = { fraction -> fraction }
            )
        )
    }

    Box(
        modifier = modifier
            .size(100.dp)
            .padding(4.dp), // Kích thước của vòng tròn
        contentAlignment = Alignment.Center
    ) {
        // Vẽ vòng tròn tiến trình
        Canvas(modifier = modifier.fillMaxSize()) {
            val strokeWidth = 16.dp.toPx() // Độ dày của vòng tròn
            val diameter = size.minDimension - strokeWidth
            val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)

            // Tạo gradient mờ dần theo chiều kim đồng hồ, bắt đầu từ 12 giờ
//            val gradientBrush = Brush.sweepGradient(
//                colors = listOf(
//                    textDetailStorage.copy(alpha = 0.2f),// Điểm bắt đầu (12 giờ)
//                    textDetailStorage.copy(alpha = 0.1f),// Điểm giữa
//                    textDetailStorage.copy(alpha = 0.05f),// Quay lại điểm ban đầu
//                ),
//                center = Offset(size.width / 2, size.height / 2) // Tâm của gradient
//            )
            val gradientBrush = Brush.linearGradient(
                colors = listOf(
                    textDetailStorage.copy(alpha = 0.1f),// Điểm bắt đầu (12 giờ)
                    textDetailStorage.copy(alpha = 0.05f),// Điểm giữa
                )
            )

            // Vẽ phần nền (bộ nhớ chưa sử dụng) với hiệu ứng mờ
            drawArc(
                brush = gradientBrush, // Màu trắng mờ
                startAngle = -90f, // Bắt đầu từ 12 giờ
                sweepAngle = 360f, // Vẽ toàn bộ vòng tròn
                useCenter = false,
                topLeft = topLeft,
                size = Size(diameter, diameter),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Vẽ phần tiến trình (bộ nhớ đã sử dụng) màu xanh
            drawArc(
                color = textSeeAll,
                startAngle = -90f, // Bắt đầu từ 12 giờ
                sweepAngle = animatedSweepAngle.value, // Góc quét động
                useCenter = false,
                topLeft = topLeft,
                size = Size(diameter, diameter),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        // Hiển thị phần trăm ở giữa
        Text(
            modifier = modifier,
            text = "${animatedPercentage.value.roundToInt()}%",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.W900,
                color = textHeaderScreenColor
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun EditSearch(
    modifier: Modifier = Modifier,
    value: String,
    keyboardOptions: KeyboardOptions,
    hintText: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier
            .padding(12.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(1.dp, color = Color.Transparent),
                shape = RoundedCornerShape(8.dp)
            ),
        keyboardOptions = keyboardOptions,
        value = value,
        leadingIcon = {

        },
        onValueChange = {

        }


    )

}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ItemStoragePreview(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
//            .background(Color.Black.copy(alpha = 0.3f))
            .padding(vertical = 80.dp)
    ) {
        ItemStorage(
//            modifier = modifier.padding(horizontal = 8.dp, vertical = 12.dp),
            typeStorage = "Internal Storage",
            usedStorage = 85,
            totalStorage = 128,
            viewDetailClick = {}
        )
        Spacer(Modifier.height(40.dp))
        AnimatedCircleProgress(
            totalStorage = 100f, // Tổng dung lượng
            usedStorage = 37f,   // Dung lượng đã sử dụng
            modifier = Modifier,
            animationDuration = 1500 // Animation kéo dài 1.5 giây
        )
    }

}