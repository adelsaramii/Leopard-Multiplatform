//package theme
//
//import androidx.compose.material.MaterialTheme
//import androidx.compose.material.typography
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.ReadOnlyComposable
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.Font
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.font.FontStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.sp
//import com.attendance.leopard.android.R
//
//private val thin = Font(R.font.vazir_thin, FontWeight.W200)
//private val light = Font(R.font.vazir_light, FontWeight.W300)
//private val regular = Font(R.font.vazir_regular, FontWeight.W400)
//private val medium = Font(R.font.vazir_medium, FontWeight.W500)
//private val bold = Font(R.font.vazir_bold, FontWeight.W700)
//
//private val fontFamily = FontFamily(fonts = listOf(thin, light, regular, medium, bold))
//
//val LeopardTypography = Typography(
//    h1 = TextStyle(
//        fontFamily = fontFamily,
//        fontStyle = FontStyle.Normal,
//        fontWeight = FontWeight(300),
//        fontSize = 96.sp,
//        lineHeight = 134.sp,
//    ), h2 = TextStyle(
//        fontFamily = fontFamily,
//        fontStyle = FontStyle.Normal,
//        fontWeight = FontWeight(300),
//        fontSize = 60.sp,
//        lineHeight = 84.sp,
//    ), h3 = TextStyle(
//        fontFamily = fontFamily,
//        fontStyle = FontStyle.Normal,
//        fontWeight = FontWeight(400),
//        fontSize = 48.sp,
//        lineHeight = 67.sp,
//    ), h4 = TextStyle(
//        fontFamily = fontFamily,
//        fontStyle = FontStyle.Normal,
//        fontWeight = FontWeight(700),
//        fontSize = 30.sp,
//        lineHeight = 42.sp,
//    ), h5 = TextStyle(
//        fontFamily = fontFamily,
//        fontStyle = FontStyle.Normal,
//        fontWeight = FontWeight(700),
//        fontSize = 24.sp,
//        lineHeight = 24.sp,
//    ), h6 = TextStyle(
//        fontFamily = fontFamily,
//        fontStyle = FontStyle.Normal,
//        fontWeight = FontWeight(700),
//        fontSize = 20.sp,
//        lineHeight = 24.sp,
//    ), subtitle1 = TextStyle(
//        fontFamily = fontFamily,
//        fontStyle = FontStyle.Normal,
//        fontWeight = FontWeight(400),
//        fontSize = 18.sp,
//        lineHeight = 20.sp,
//    ), subtitle2 = TextStyle(
//        fontFamily = fontFamily,
//        fontStyle = FontStyle.Normal,
//        fontWeight = FontWeight(700),
//        fontSize = 14.sp,
//        lineHeight = 24.sp,
//    ), body1 = TextStyle(
//        fontFamily = fontFamily,
//        fontStyle = FontStyle.Normal,
//        fontWeight = FontWeight(700),
//        fontSize = 16.sp,
//        lineHeight = 16.sp,
//    ), body2 = TextStyle(
//        fontFamily = fontFamily,
//        fontStyle = FontStyle.Normal,
//        fontWeight = FontWeight(400),
//        fontSize = 14.sp,
//        lineHeight = 24.sp,
//    ), caption = TextStyle(
//        fontFamily = fontFamily,
//        fontStyle = FontStyle.Normal,
//        fontWeight = FontWeight(300),
//        fontSize = 12.sp,
//        lineHeight = 16.sp,
//    ), button = TextStyle(
//        fontFamily = fontFamily,
//        fontStyle = FontStyle.Normal,
//        fontWeight = FontWeight(700),
//        fontSize = 16.sp,
//        lineHeight = 20.sp,
//        color = white,
//    )
//)
//val MaterialTheme.typography: Typography
//    @Composable @ReadOnlyComposable get() = LeopardTypography
