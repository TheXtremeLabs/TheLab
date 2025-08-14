package com.riders.thelab.core.ui.compose.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

///////////////////////////////////////
//
// MOBILE
//
///////////////////////////////////////
val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)


///////////////////////////////////////
//
// TV
//
///////////////////////////////////////
val ShapesTV = androidx.tv.material3.Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)