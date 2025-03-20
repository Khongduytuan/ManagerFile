package com.eagletech.managerfile.ui.widgets

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.eagletech.managerfile.R
import com.eagletech.managerfile.ui.theme.iconAndTextBottomNav


@Composable
fun BottomNavigation(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.background(Color.White),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ItemBottomNav(
            modifier = modifier,
            isSelected = true,
            iconSelected = R.drawable.ic_home_selected,
            iconDefault = R.drawable.ic_home_default,
            title = "Home",
            width = (LocalConfiguration.current.screenWidthDp / 5).dp,
            height = 60.dp
        ) { }

        ItemBottomNav(
            modifier = modifier,
            isSelected = false,
            iconSelected = R.drawable.ic_file_selected,
            iconDefault = R.drawable.ic_file_default,
            title = "Files",
            width = (LocalConfiguration.current.screenWidthDp / 5).dp,
            height = 60.dp
        ) { }
        ItemBottomNav(
            modifier = modifier,
            isSelected = false,
            iconSelected = R.drawable.ic_cloud_selected,
            iconDefault = R.drawable.ic_cloud_default,
            title = "Cloud",
            width = (LocalConfiguration.current.screenWidthDp / 5).dp,
            height = 60.dp
        ) { }
        ItemBottomNav(
            modifier = modifier,
            isSelected = false,
            iconSelected = R.drawable.ic_clean_selected,
            iconDefault = R.drawable.ic_clean_default,
            title = "Clean",
            width = (LocalConfiguration.current.screenWidthDp / 5).dp,
            height = 60.dp
        ) { }

    }
}


@Composable
fun ItemBottomNav(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    iconDefault: Int,
    iconSelected: Int,
    title: String,
    width: Dp,
    height: Dp,
    onClick: () -> Unit
) {

    Box(
        modifier = modifier
            .padding(top = 8.dp, start = 4.dp, end = 4.dp)
            .size(width = width, height = height)
            .wrapContentSize(),
        contentAlignment = Alignment.Center
    ) {
        ConstraintLayout(modifier = modifier.fillMaxSize()) {
            val (iconID, titleID) = createRefs()
            Image(
                modifier = modifier
                    .size(width / 4)
                    .clickable {
                        onClick()
                    }
                    .constrainAs(iconID) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                    },
                painter = painterResource(id = if (isSelected) iconSelected else iconDefault),
                contentDescription = "icon bottom nav",
            )
            Text(
                modifier = modifier
                    .clickable { onClick() }
                    .constrainAs(titleID) {
                        top.linkTo(iconID.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                text = title,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Start,
                color = if (isSelected) iconAndTextBottomNav else Color.Black
            )

        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewItemBottomNav(modifier: Modifier = Modifier) {
    var isSelected by remember { mutableStateOf(false) }
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        ItemBottomNav(
            modifier = modifier,
            isSelected = isSelected,
            iconDefault = R.drawable.ic_home_default,
            iconSelected = R.drawable.ic_home_selected,
            title = "Home",
            width = (LocalConfiguration.current.screenWidthDp / 4).dp,
            height = 60.dp
        ) {
            isSelected = !isSelected
        }

        BottomNavigation(modifier = modifier)
    }


}