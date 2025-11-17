// ==========================================================
// CONFIGURAÇÃO DA URL DA API
// ==========================================================
const API_URL = 'http://localhost:8080/api/status';

// ==========================================================
// REFERÊNCIAS AOS ELEMENTOS DO DOM
// ==========================================================
const statusForm = document.getElementById('statusForm');
const statusIdInput = document.getElementById('statusId');
const descricaoInput = document.getElementById('descricao');
const statusTableBody = document.querySelector('#statusTable tbody');
const statusCountBadge = document.getElementById('statusCount');
const emptyState = document.getElementById('emptyState');
const confirmModal = document.getElementById('confirmModal');
const confirmYesBtn = document.getElementById('confirmYes');
const confirmNoBtn = document.getElementById('confirmNo');
const notification = document.getElementById('notification');

let statusIdToDelete = null;

// ==========================================================
// INICIALIZAÇÃO E EVENTOS
// ==========================================================
document.addEventListener('DOMContentLoaded', () => {
    carregarStatus();
    statusForm.addEventListener('submit', salvarStatus);
    confirmYesBtn.addEventListener('click', () => deletarStatus(statusIdToDelete));
    confirmNoBtn.addEventListener('click', fecharModalConfirmacao);
});

// ==========================================================
// FUNÇÕES DE CARREGAMENTO E RENDERIZAÇÃO
// ==========================================================
async function carregarStatus() {
    try {
        const response = await fetch(API_URL);
        if (!response.ok) throw new Error('Erro ao buscar status.');
        const statusList = await response.json();
        renderizarTabela(statusList);
    } catch (error) {
        showNotification(`Erro ao carregar status: ${error.message}`, 3000, 'error');
    }
}

function renderizarTabela(statusList) {
    statusTableBody.innerHTML = '';
    emptyState.style.display = statusList.length === 0 ? 'block' : 'none';


    statusList.sort((a, b) => a.id - b.id);

    statusList.forEach(status => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${status.id}</td>
            <td>${status.descricao}</td>
            <td class="actions">
                <button onclick="editarStatus(${status.id})" class="btn btn-sm btn-warning">
                    <i class="fas fa-edit"></i>
                </button>
                <button onclick="abrirModalConfirmacao(${status.id})" class="btn btn-sm btn-danger">
                    <i class="fas fa-trash"></i>
                </button>
            </td>
        `;
        statusTableBody.appendChild(row);
    });

    statusCountBadge.textContent = `${statusList.length} status`;
}

// ==========================================================
// FUNÇÕES DE CRUD (Criar, Ler, Atualizar, Deletar)
// ==========================================================
async function salvarStatus(event) {
    event.preventDefault();

    const id = statusIdInput.value;
    const descricao = descricaoInput.value;

    if (!descricao.trim()) {
        showNotification('A descrição do status não pode ser vazia.', 3000, 'error');
        return;
    }

    const status = {
        id: id ? parseInt(id) : null,
        descricao: descricao
    };

    const method = id ? 'PUT' : 'POST';
    const url = id ? `${API_URL}/${id}` : API_URL;

    try {
        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(status)
        });

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            throw new Error(errorData.message || 'Erro ao salvar o status.');
        }

        showNotification('Status salvo com sucesso!', 3000, 'success');
        limparFormulario();
        carregarStatus();

    } catch (error) {
        showNotification(error.message, 4000, 'error');
    }
}

async function editarStatus(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`);
        if (!response.ok) throw new Error('Status não encontrado.');
        const status = await response.json();

        statusIdInput.value = status.id;
        descricaoInput.value = status.descricao;

        window.scrollTo({ top: 0, behavior: 'smooth' });
    } catch (error) {
        showNotification(`Erro ao carregar status para edição: ${error.message}`, 3000, 'error');
    }
}

async function deletarStatus(id) {
    if (!id) return;

    try {
        const response = await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
        
        if (!response.ok) {
             const errorData = await response.json().catch(() => ({}));
             throw new Error(errorData.message || 'Erro ao deletar o status.');
        }
        
        showNotification('Status deletado com sucesso!', 3000, 'success');
        fecharModalConfirmacao();
        carregarStatus();

    } catch (error) {
        showNotification(`Erro: ${error.message}`, 4000, 'error');
        fecharModalConfirmacao();
    }
}

// ==========================================================
// FUNÇÕES AUXILIARES (Modais, Formulário, Notificações)
// ==========================================================
function limparFormulario() {
    statusForm.reset();
    statusIdInput.value = '';
}

function abrirModalConfirmacao(id) {
    statusIdToDelete = id;
    confirmModal.classList.remove('hidden');
}

function fecharModalConfirmacao() {
    statusIdToDelete = null;
    confirmModal.classList.add('hidden');
}

function showNotification(message, duration = 3000, type = 'success') {
    const notification = document.getElementById('notification');
    if (!notification) return;
    notification.textContent = message;
    notification.className = `notification show ${type}`;
    setTimeout(() => {
        notification.className = 'notification hidden';
    }, duration);
}
