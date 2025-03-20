package com.eagletech.managerfile.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.eagletech.managerfile.R
import com.eagletech.managerfile.SortType
import com.eagletech.managerfile.ui.theme.ManagerFileTheme
import com.eagletech.managerfile.ui.theme.searchColor
import com.eagletech.managerfile.ui.theme.textHeaderScreenColor
import com.eagletech.managerfile.ui.theme.textTitleStorage
import com.eagletech.managerfile.ui.widgets.BottomNavigation
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileManagerApp(
    modifier: Modifier = Modifier,
    currentDirectory: File,
    files: List<File>,
    onDirectoryChanged: (File) -> Unit,
    onClickMore: (File) -> Unit,
    onClickItem: (File) -> Unit,
    onSortChanged: (SortType) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBarFilesScreen(
                modifier = modifier.statusBarsPadding(),
                onSortChanged = onSortChanged
            )
        },
        bottomBar = {
            BottomNavigation(modifier = modifier.navigationBarsPadding())
        },
        content = { paddingValues ->
            ConstraintLayout(modifier = modifier.padding(paddingValues)) {
                val (tabFilter, columFiles) = createRefs()
                TabFilter(modifier = modifier
                    .padding(vertical = 8.dp)
                    .constrainAs(tabFilter) {
                        top.linkTo(parent.top, margin = 8.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })
                LazyColumn(
                    modifier = modifier
                        .navigationBarsPadding()
                        .padding(vertical = 12.dp)
                        .wrapContentSize()
                        .constrainAs(columFiles) {
                            top.linkTo(tabFilter.bottom, margin = 16.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        },
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(files) { file ->
                        if (!file.isFile) {
                            ItemFolder(
                                modifier = modifier,
                                nameFolder = file.name,
                                numberItems = getNumberOfItems(file),
                                sizeFolder = formatSize(getFolderSize(file)),
                                isFavorites = false,
                                onClickMore = { onClickMore(file) },
                                onCLickItem = { onClickItem(file) }
                            )
                        } else {
                            ItemFile(
                                modifier = modifier,
                                nameFile = file.name,
                                sizeFile = formatSize(file.length()),
                                onCLickItem = { onClickItem(file) }
                            )
                        }

                        Spacer(
                            modifier = modifier
                                .background(textHeaderScreenColor)
                                .height(4.dp)
                        )
                    }
                }
            }

        }
    )
}

@Composable
fun ItemFolder(
    modifier: Modifier = Modifier,
    nameFolder: String,
    numberItems: Int,
    sizeFolder: String,
    isFavorites: Boolean = false,
    onCLickItem: () -> Unit,
    onClickMore: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(horizontal = 12.dp, vertical = 12.dp)
            .fillMaxWidth()
            .clickable {
                onCLickItem()
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = modifier.weight(9f),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = modifier,
                painter = painterResource(
                    id =
                    if (isFavorites)
                        R.drawable.ic_folder_favorites
                    else
                        R.drawable.ic_folder_default
                ),
                contentDescription = "icon folder",
            )

            Column(
                modifier = modifier
                    .padding(start = 12.dp, end = 24.dp)
                    .wrapContentHeight()
                    .wrapContentWidth(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    modifier = modifier,
                    text = nameFolder,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.W400,
                        color = textHeaderScreenColor,
                        fontSize = 12.sp
                    ),
                )
                Text(
                    modifier = modifier,
                    text = "$numberItems item • $sizeFolder",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.W300,
                        color = textTitleStorage,
                        fontSize = 10.sp
                    ),
                )

            }


        }

        Image(
            modifier = modifier
                .weight(1f)
                .clickable { onClickMore() },
            painter = painterResource(id = R.drawable.ic_more),
            contentDescription = "icon more",
        )
    }

}


@Composable
fun ItemFile(
    modifier: Modifier = Modifier,
    nameFile: String,
    sizeFile: String,
    onCLickItem: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(horizontal = 12.dp, vertical = 12.dp)
            .fillMaxWidth()
            .clickable {
                onCLickItem()
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = modifier.weight(7f),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = modifier,
                painter = painterResource(
                    R.drawable.ic_file
                ),
                contentDescription = "icon file",
            )
            Text(
                modifier = modifier
                    .padding(start = 12.dp, end = 24.dp)
                    .wrapContentHeight()
                    .wrapContentWidth(),
                text = nameFile,
                maxLines = 1,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.W400,
                    color = textHeaderScreenColor,
                    fontSize = 12.sp
                ),
            )
        }

        Text(
            modifier = modifier.weight(3f),
            text = sizeFile.toString(),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.W400,
                color = textHeaderScreenColor,
                fontSize = 12.sp
            ),
        )
    }


}

@Composable
fun TabFilter(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = modifier,
                text = "A - Z ",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.W400,
                    color = searchColor
                ),
            )
            Image(
                modifier = modifier.padding(start = 6.dp),
                painter = painterResource(id = R.drawable.ic_filter),
                contentDescription = "icon filter",
            )
        }
        Image(
            modifier = modifier,
            painter = painterResource(id = R.drawable.ic_type_view),
            contentDescription = "icon filter",
        )


    }

}

@ExperimentalMaterial3Api
@Composable
fun TopAppBarFilesScreen(
    modifier: Modifier = Modifier,
    onSortChanged: (SortType) -> Unit = {}
) {
//    var expanded by remember { mutableStateOf(false) }
//    TopAppBar(
//        title = { Text("File Manager") },
//        actions = {
//            Box {
//                IconButton(onClick = { expanded = true }) {
//                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Sort")
//                }
//                DropdownMenu(
//                    expanded = expanded,
//                    onDismissRequest = { expanded = false }
//                ) {
//                    DropdownMenuItem(
//                        onClick = {
//                            onSortChanged(SortType.NAME_ASC)
//                            expanded = false
//                        }, text = {
//                            Text("Name (A-Z)")
//                        })
//                    DropdownMenuItem(onClick = {
//                        onSortChanged(SortType.NAME_DESC)
//                        expanded = false
//                    }, text = {
//                        Text("Name (Z-A)")
//                    })
//                    DropdownMenuItem(onClick = {
//                        onSortChanged(SortType.SIZE_ASC)
//                        expanded = false
//                    }, text = {
//                        Text("Size (Small to Large)")
//                    })
//                    DropdownMenuItem(onClick = {
//                        onSortChanged(SortType.SIZE_DESC)
//                        expanded = false
//                    }, text = {
//                        Text("Size (Large to Small)")
//                    })
//                }
//            }
//        }
//    )
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 12.dp, vertical = 4.dp),
    ) {
        val (titleTopAppBar, layoutItem) = createRefs()

        Text(
            modifier = modifier.constrainAs(titleTopAppBar) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                bottom.linkTo(parent.bottom)
            },
            text = "Internal Storage",
            style = MaterialTheme.typography.titleLarge,
        )


        Row(
            modifier = modifier
                .wrapContentHeight()
                .constrainAs(layoutItem) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                modifier = modifier,
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = "icon add",
            )
            Spacer(modifier = modifier.width(16.dp))
            Image(
                modifier = modifier,
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "icon search",
            )

        }
    }
}

// Hàm tính số lượng file/thư mục con
fun getNumberOfItems(file: File): Int {
    return file.listFiles()?.size ?: 0
}

// Hàm tính kích thước tổng của thư mục (bao gồm đệ quy)
fun getFolderSize(file: File): Long {
    if (file.isFile) return file.length()
    var size: Long = 0
    file.listFiles()?.forEach { child ->
        size += getFolderSize(child)
    }
    return size
}

// Hàm chuyển đổi kích thước byte sang định dạng dễ đọc (KB, MB, GB)
fun formatSize(size: Long): String {
    return when {
        size >= 1_073_741_824 -> String.format("%.2f GB", size / 1_073_741_824.0)
        size >= 1_048_576 -> String.format("%.2f MB", size / 1_048_576.0)
        size >= 1_024 -> String.format("%.2f KB", size / 1_024.0)
        else -> "$size bytes"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewTopAppBarFilesScreen(modifier: Modifier = Modifier) {
    ManagerFileTheme {
        TopAppBarFilesScreen(modifier = modifier)
    }
}


// Code test man FilesScreen
