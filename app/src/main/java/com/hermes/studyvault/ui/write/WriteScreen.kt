package com.hermes.studyvault.ui.write

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hermes.studyvault.R
import com.hermes.studyvault.data.local.StudyVaultDatabaseProvider
import com.hermes.studyvault.data.local.entity.NoteEntity
import com.hermes.studyvault.domain.planner.PlannerItemFactory
import java.time.Instant
import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun WriteScreen() {
    val context = LocalContext.current
    val database = remember(context) { StudyVaultDatabaseProvider.get(context) }
    val notes by database.noteDao().observeAll().collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    var feedback by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        Text(stringResource(R.string.write_title), style = MaterialTheme.typography.headlineMedium)
        Text(
            text = stringResource(R.string.write_body),
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
        )
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.note_title)) },
            singleLine = true,
        )
        OutlinedTextField(
            value = body,
            onValueChange = { body = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            label = { Text(stringResource(R.string.note_body)) },
            minLines = 5,
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            onClick = {
                if (title.isBlank() || body.isBlank()) {
                    feedback = context.getString(R.string.note_title_required)
                    return@Button
                }
                val note = PlannerItemFactory.createNote(
                    id = UUID.randomUUID().toString(),
                    title = title,
                    body = body,
                    now = Instant.now(),
                )
                scope.launch {
                    withContext(Dispatchers.IO) {
                        database.noteDao().insert(note)
                    }
                    title = ""
                    body = ""
                    feedback = context.getString(R.string.note_saved)
                }
            },
        ) {
            Text(stringResource(R.string.save_note))
        }

        if (feedback.isNotBlank()) {
            Text(
                text = feedback,
                modifier = Modifier.padding(top = 10.dp),
                color = MaterialTheme.colorScheme.primary,
            )
        }

        Text(
            text = stringResource(R.string.saved_notes),
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
            style = MaterialTheme.typography.titleMedium,
        )
        if (notes.isEmpty()) {
            Text(stringResource(R.string.empty_notes))
        } else {
            notes.forEach { note ->
                NoteCard(note)
            }
        }
    }
}

@Composable
private fun NoteCard(note: NoteEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(note.title, style = MaterialTheme.typography.titleSmall)
            Text(
                text = note.bodyBlocksJson.toPlainNoteText(),
                modifier = Modifier.padding(top = 4.dp),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

private fun String.toPlainNoteText(): String {
    return replace("[", "")
        .replace("]", "")
        .replace("{", "")
        .replace("}", "")
        .replace("\"type\":\"paragraph\",", "")
        .replace("\"text\":\"", "")
        .replace("\"", "")
}
