package com.allianz.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.allianz.data.model.DiscordUser
import com.allianz.ui.about.AboutScreen

// Colores del tema estilo TIDAL
private val TidalBackground = Color(0xFF0D0E12)
private val TidalCardBg = Color(0xFF181A20)
private val TidalCyan = Color(0xFF00E5FF)
private val TidalTextPrimary = Color(0xFFF0F0F5)
private val TidalTextSecondary = Color(0xFF8A8D9B)
private val DiscordBlurple = Color(0xFF5865F2)

data class MusicItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val imageUrl: String,
    val isMax: Boolean = false
)

data class ArtistItem(
    val id: String,
    val name: String,
    val imageUrl: String
)

@Composable
fun MusicDashboard(
    user: DiscordUser,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showProfileDialog by remember { mutableStateOf(false) }
    var showAboutScreen by remember { mutableStateOf(false) }

    if (showAboutScreen) {
        AboutScreen(
            onBackClick = { showAboutScreen = false }
        )
    } else {
        // Modal de perfil de Discord al presionar el avatar
        if (showProfileDialog) {
            DiscordProfileDialog(
                user = user,
                onDismiss = { showProfileDialog = false },
                onLogout = {
                    showProfileDialog = false
                    onLogout()
                },
                onOpenAbout = {
                    showProfileDialog = false
                    showAboutScreen = true
                }
            )
        }

        Scaffold(
            containerColor = TidalBackground,
            bottomBar = { MiniPlayer() },
            modifier = modifier.fillMaxSize()
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                // Header con logo TIDAL/ALLIANZ y botón de perfil de Discord en la esquina superior derecha
                item {
                    DashboardHeader(
                        user = user,
                        onProfileClick = { showProfileDialog = true },
                        onAboutClick = { showAboutScreen = true }
                    )
                }

                // Saludo de bienvenida personalizado
                item {
                    GreetingSection(user = user)
                }

                // Grilla de Acceso Rápido
                item {
                    QuickAccessSection()
                }

                // Sección: Escuchado Recientemente
                item {
                    SectionHeader(title = "Escuchado recientemente")
                    RecentList()
                }

                // Sección: Álbumes Destacados (Hi-Res / MAX)
                item {
                    SectionHeader(title = "Novedades en Alta Fidelidad (MAX)")
                    FeaturedAlbumsList()
                }

                // Sección: Artistas Populares
                item {
                    SectionHeader(title = "Artistas populares")
                    ArtistsList()
                }
            }
        }
    }
}

@Composable
private fun DashboardHeader(
    user: DiscordUser,
    onProfileClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Logo estilo TIDAL
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "ALLIANZ",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = TidalTextPrimary,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                color = TidalCyan,
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = "HI-FI",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            // Botón "Acerca de"
            Surface(
                color = TidalCardBg,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .padding(end = 10.dp)
                    .clickable { onAboutClick() }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "ℹ️",
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Acerca de",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TidalTextPrimary
                    )
                }
            }

            // Botón de Perfil con el Avatar de Discord en la esquina superior
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .border(2.dp, TidalCyan, CircleShape)
                    .clickable { onProfileClick() }
            ) {
                AsyncImage(
                    model = user.getAvatarUrl(),
                    contentDescription = "Perfil de Discord",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun GreetingSection(user: DiscordUser) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Buenas noches, ${user.globalName ?: user.username} 👋",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TidalTextPrimary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Calidad de audio activa: Flac 24-bit / 192 kHz",
            fontSize = 13.sp,
            color = TidalCyan,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun QuickAccessSection() {
    val items = remember {
        listOf(
            MusicItem("1", "Mis Favoritos", "342 canciones", "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=300"),
            MusicItem("2", "Mix Diario 1", "Daft Punk, The Weeknd", "https://images.unsplash.com/photo-1614613535308-eb5fbd3d2c17?w=300"),
            MusicItem("3", "Novedades Hi-Res", "Los lanzamientos de la semana", "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=300"),
            MusicItem("4", "Top España", "Lo más escuchado", "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=300")
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            QuickAccessCard(item = items[0], modifier = Modifier.weight(1f))
            QuickAccessCard(item = items[1], modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            QuickAccessCard(item = items[2], modifier = Modifier.weight(1f))
            QuickAccessCard(item = items[3], modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun QuickAccessCard(item: MusicItem, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = TidalCardBg),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .height(56.dp)
            .clickable { }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp))
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = item.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TidalTextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = TidalTextPrimary,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp)
    )
}

@Composable
private fun RecentList() {
    val list = remember {
        listOf(
            MusicItem("1", "After Hours", "The Weeknd", "https://images.unsplash.com/photo-1614613535308-eb5fbd3d2c17?w=400"),
            MusicItem("2", "Random Access Memories", "Daft Punk", "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=400"),
            MusicItem("3", "Cyberpunk Synthwave", "Varios Artistas", "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=400"),
            MusicItem("4", "Acoustic Sessions", "John Mayer", "https://images.unsplash.com/photo-1511379938547-c1f69419868d?w=400")
        )
    }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(list) { item ->
            AlbumCard(item = item, size = 140.dp)
        }
    }
}

@Composable
private fun FeaturedAlbumsList() {
    val list = remember {
        listOf(
            MusicItem("1", "Utopia (MAX Edition)", "Travis Scott", "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=400", isMax = true),
            MusicItem("2", "Renaissance Hi-Res", "Beyoncé", "https://images.unsplash.com/photo-1493225457124-a3eb161ffa5f?w=400", isMax = true),
            MusicItem("3", "Midnight Memories", "Taylor Swift", "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=400", isMax = true)
        )
    }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(list) { item ->
            AlbumCard(item = item, size = 160.dp)
        }
    }
}

@Composable
private fun AlbumCard(item: MusicItem, size: androidx.compose.ui.unit.Dp) {
    Column(
        modifier = Modifier
            .width(size)
            .clickable { }
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(RoundedCornerShape(12.dp))
                .background(TidalCardBg)
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            if (item.isMax) {
                Surface(
                    color = TidalCyan,
                    shape = RoundedCornerShape(topStart = 0.dp, bottomStart = 6.dp),
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Text(
                        text = "MAX",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = item.title,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = TidalTextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = item.subtitle,
            fontSize = 12.sp,
            color = TidalTextSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun ArtistsList() {
    val artists = remember {
        listOf(
            ArtistItem("1", "The Weeknd", "https://images.unsplash.com/photo-1493225457124-a3eb161ffa5f?w=300"),
            ArtistItem("2", "Daft Punk", "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=300"),
            ArtistItem("3", "Rosalía", "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=300"),
            ArtistItem("4", "Bad Bunny", "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=300")
        )
    }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(artists) { artist ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { }
            ) {
                AsyncImage(
                    model = artist.imageUrl,
                    contentDescription = artist.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(TidalCardBg)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = artist.name,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = TidalTextPrimary
                )
            }
        }
    }
}

@Composable
private fun MiniPlayer() {
    Surface(
        color = Color(0xFF14161C),
        tonalElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            // Barra de progreso del reproductor
            LinearProgressIndicator(
                progress = { 0.42f },
                color = TidalCyan,
                trackColor = Color.Transparent,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    AsyncImage(
                        model = "https://images.unsplash.com/photo-1614613535308-eb5fbd3d2c17?w=200",
                        contentDescription = "Carátula",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(6.dp))
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Blinding Lights",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TidalTextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "The Weeknd • FLAC 24-bit",
                            fontSize = 11.sp,
                            color = TidalCyan,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "▶",
                        fontSize = 20.sp,
                        color = TidalTextPrimary,
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "⏭",
                        fontSize = 20.sp,
                        color = TidalTextPrimary,
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { }
                    )
                }
            }
        }
    }
}

/**
 * Diálogo emergente de perfil al presionar la foto de Discord
 */
@Composable
fun DiscordProfileDialog(
    user: DiscordUser,
    onDismiss: () -> Unit,
    onLogout: () -> Unit,
    onOpenAbout: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = TidalCardBg),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Insignia de Discord
                Surface(
                    color = DiscordBlurple,
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = "DISCORD OAUTH2",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Avatar de Discord
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(2.dp, DiscordBlurple, CircleShape)
                ) {
                    AsyncImage(
                        model = user.getAvatarUrl(),
                        contentDescription = "Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Nombre Global / Username
                Text(
                    text = user.globalName ?: user.username,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TidalTextPrimary
                )

                Text(
                    text = "@${user.username}",
                    fontSize = 14.sp,
                    color = DiscordBlurple,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ID y Email
                Text(
                    text = "ID: ${user.id}",
                    fontSize = 12.sp,
                    color = TidalTextSecondary
                )

                user.email?.let { email ->
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = email,
                        fontSize = 12.sp,
                        color = TidalTextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Botón para ir a Acerca De
                OutlinedButton(
                    onClick = onOpenAbout,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Acerca de (Equipo & Materia)",
                        fontWeight = FontWeight.SemiBold,
                        color = TidalCyan
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Botón Cerrar Sesión
                Button(
                    onClick = onLogout,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE53935),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Cerrar sesión",
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Cerrar",
                        color = TidalTextSecondary
                    )
                }
            }
        }
    }
}
