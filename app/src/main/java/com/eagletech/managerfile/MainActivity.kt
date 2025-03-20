package com.eagletech.managerfile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.eagletech.managerfile.ui.screens.FileManagerApp
import com.eagletech.managerfile.ui.screens.ItemFolder
import com.eagletech.managerfile.ui.screens.TabFilter
import com.eagletech.managerfile.ui.screens.TopAppBarFilesScreen
import com.eagletech.managerfile.ui.screens.getFolderSize
import com.eagletech.managerfile.ui.theme.ManagerFileTheme
import com.eagletech.managerfile.ui.theme.textHeaderScreenColor
import com.eagletech.managerfile.ui.widgets.BottomNavigation
import java.io.File

class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) loadFileList(Environment.getExternalStorageDirectory())
    }

    private val manageExternalStorageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Environment.isExternalStorageManager()) {
            loadFileList(Environment.getExternalStorageDirectory())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ManagerFileTheme {
//                val windowSizeClass = calculateWindowSizeClass(this)
                var currentDirectory by remember { mutableStateOf(Environment.getExternalStorageDirectory()) }
                var sortType by remember { mutableStateOf(SortType.NAME_ASC) } // Thêm state sắp xếp
                var files by remember(currentDirectory, sortType) {
                    mutableStateOf(getFilesList(currentDirectory, sortType))
                }
//                var files by remember { mutableStateOf(getFilesList(currentDirectory)) }
                FileManagerApp(
                    modifier = Modifier,
                    currentDirectory = currentDirectory,
                    files = files,
                    onDirectoryChanged = { newDirectory ->
                        currentDirectory = newDirectory
                        files = getFilesList(newDirectory)
                    },
                    onClickMore = { file ->
                        // Xử lý sự kiện More
                    },
                    onClickItem = { file ->
                        if (file.isDirectory) {
                            currentDirectory = file
                            files = getFilesList(file)
                        }
                    },
                    onSortChanged = { newSortType ->
                        sortType = newSortType
                        files = getFilesList(currentDirectory, sortType)
                    }
                )
            }
            requestStoragePermission()
        }
    }


    private fun requestStoragePermission() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                if (!Environment.isExternalStorageManager()) {
                    val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                    manageExternalStorageLauncher.launch(intent)
                } else {
                    loadFileList(Environment.getExternalStorageDirectory())
                }
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                when {
                    ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) ==
                            PackageManager.PERMISSION_GRANTED -> {
                        loadFileList(Environment.getExternalStorageDirectory())
                    }

                    ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) -> {
                        // Hiển thị lý do yêu cầu quyền (tùy chọn)
                    }

                    else -> {
                        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                }
            }

            else -> {
                loadFileList(Environment.getExternalStorageDirectory())
            }
        }
    }

    private fun loadFileList(directory: File) {
        // Cập nhật danh sách file (sẽ được phản ánh trong UI qua remember/mutableState)
    }

//    fun getFilesList(directory: File): List<File> {
//        return directory.listFiles()?.toList() ?: emptyList()
//    }

    fun getFilesList(directory: File, sortType: SortType = SortType.NAME_ASC): List<File> {
        val fileList: List<File> = directory.listFiles()?.toList() ?: emptyList()
        return when (sortType) {
            SortType.NAME_ASC -> fileList.sortedWith { file1, file2 ->
                when {
                    file1.isDirectory == file2.isDirectory -> file1.name.lowercase()
                        .compareTo(file2.name.lowercase())

                    file1.isDirectory -> -1 // Thư mục lên trước
                    else -> 1
                }
            }

            SortType.NAME_DESC -> fileList.sortedWith { file1, file2 ->
                when {
                    file1.isDirectory == file2.isDirectory -> file2.name.lowercase()
                        .compareTo(file1.name.lowercase())

                    file1.isDirectory -> -1
                    else -> 1
                }
            }

            SortType.SIZE_ASC -> fileList.sortedWith { file1, file2 ->
                when {
                    file1.isDirectory == file2.isDirectory -> getFolderSize(file1).compareTo(
                        getFolderSize(file2)
                    )

                    file1.isDirectory -> -1
                    else -> 1
                }
            }

            SortType.SIZE_DESC -> fileList.sortedWith { file1, file2 ->
                when {
                    file1.isDirectory == file2.isDirectory -> getFolderSize(file2).compareTo(
                        getFolderSize(file1)
                    )

                    file1.isDirectory -> -1
                    else -> 1
                }
            }
        }
    }
}

// Các loại sắp xếp file
enum class SortType {
    NAME_ASC, // A-Z
    NAME_DESC, // Z-A
    SIZE_ASC, // Nhỏ đến lớn
    SIZE_DESC // Lớn đến nhỏ
}

// Data class cho các item trong Bottom Navigation
data class NavigationItem(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)


