package com.allianz.ui.about

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

private val TidalBackground = Color(0xFF0D0E12)
private val TidalCardBg = Color(0xFF181A20)
private val TidalCyan = Color(0xFF00E5FF)
private val TidalTextPrimary = Color(0xFFF0F0F5)
private val TidalTextSecondary = Color(0xFF8A8D9B)

data class TeamMember(
    val name: String,
    val role: String,
    val catImageUrl: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val teamMembers = listOf(
        TeamMember(
            name = "Jose Alberto Gomez Vazquez",
            role = "Integrante del equipo",
            catImageUrl = "https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?w=400"
        ),
        TeamMember(
            name = "Sergio Armando Lopez Hernandez",
            role = "Integrante del equipo",
            catImageUrl = "https://images.unsplash.com/photo-1573865526739-10659fec78a5?w=400"
        ),
        TeamMember(
            name = "Paulina Mendez Hernandez",
            role = "Integrante del equipo",
            catImageUrl = "https://images.unsplash.com/photo-1533738363-b7f9aef128ce?w=400"
        ),
        TeamMember(
            name = "Abner Ismael Rodriguez Gomez",
            role = "Integrante del equipo",
            catImageUrl = "https://images.unsplash.com/photo-1561948955-570b270e7c36?w=400"
        )
    )

    Scaffold(
        containerColor = TidalBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Acerca de",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TidalTextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Text(
                            text = "←",
                            fontSize = 24.sp,
                            color = TidalCyan,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TidalBackground
                )
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Tarjeta de Información de la Materia
            item {
                CourseInfoCard()
            }

            // Título de sección de Integrantes
            item {
                Text(
                    text = "Equipo de Trabajo",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TidalTextPrimary,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
            }

            // Lista de los 4 integrantes con sus fotos de gatitos
            items(teamMembers) { member ->
                TeamMemberCard(member = member)
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun CourseInfoCard() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = TidalCardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Surface(
                color = TidalCyan,
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = "UNIDAD 2",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Desarrollo móvil integral 2",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TidalTextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Jetpack Compose + Discord",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = TidalCyan
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Docente:",
                    fontSize = 13.sp,
                    color = TidalTextSecondary,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Dr. Armando Mendez Morales",
                    fontSize = 14.sp,
                    color = TidalTextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun TeamMemberCard(member: TeamMember) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = TidalCardBg),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Foto personalizada de gatito
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .border(2.dp, TidalCyan, CircleShape)
                    .background(Color.Gray.copy(alpha = 0.2f))
            ) {
                AsyncImage(
                    model = member.catImageUrl,
                    contentDescription = "Gatito de ${member.name}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = member.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TidalTextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = member.role,
                    fontSize = 13.sp,
                    color = TidalTextSecondary
                )
            }
        }
    }
}
