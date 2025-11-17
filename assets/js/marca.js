// ==========================================================
// CONFIGURAÇÃO DAS URLs DA API
// ==========================================================
const API_URL = 'http://localhost:8080/api/marca';

const marcaForm = document.getElementById('marcaForm');
const marcaIdInput = document.getElementById('marcaId');
const nomeInput = document.getElementById('nome');
const marcasTableBody = document.querySelector('#marcasTable tbody');
const marcaCountBadge = document.getElementById('marcaCount');
const emptyState = document.getElementById('emptyState');
const confirmModal = document.getElementById('confirmModal');
const confirmYesBtn = document.getElementById('confirmYes');
const confirmNoBtn = document.getElementById('confirmNo');
const notification = document.getElementById('notification');

let marcaIdToDelete = null;

// ==========================================================
// INICIALIZAÇÃO E EVENTOS
// ==========================================================
document.addEventListener('DOMContentLoaded', () => {
    carregarMarcas();
    marcaForm.addEventListener('submit', salvarMarca);
    confirmYesBtn.addEventListener('click', () => deletarMarca(marcaIdToDelete));
    confirmNoBtn.addEventListener('click', fecharModalConfirmacao);
});

// ==========================================================
// FUNÇÕES DE CARREGAMENTO E RENDERIZAÇÃO
// ==========================================================
async function carregarMarcas() {
    try {
        const response = await fetch(API_URL);
        if (!response.ok) throw new Error('Erro ao buscar marcas.');
        const marcas = await response.json();
        renderizarTabela(marcas);
    } catch (error) {
        showNotification(`Erro ao carregar marcas: ${error.message}`, 3000, 'error');
    }
}

function renderizarTabela(marcas) {
    marcasTableBody.innerHTML = '';
    emptyState.style.display = marcas.length === 0 ? 'block' : 'none';

    marcas.sort((a, b) => a.id - b.id);

    marcas.forEach(marca => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${marca.id}</td>
            <td>${marca.nome}</td>
            <td class="actions">
                <button onclick="editarMarca(${marca.id})" class="btn btn-sm btn-warning">
                    <i class="fas fa-edit"></i>
                </button>
                <button onclick="abrirModalConfirmacao(${marca.id})" class="btn btn-sm btn-danger">
                    <i class="fas fa-trash"></i>
                </button>
            </td>
        `;
        marcasTableBody.appendChild(row);
    });

    marcaCountBadge.textContent = `${marcas.length} marcas`;
}

// ==========================================================
// FUNÇÕES DE CRUD (Criar, Ler, Atualizar, Deletar)
// ==========================================================
async function salvarMarca(event) {
    event.preventDefault();

    const id = marcaIdInput.value;
    const nome = nomeInput.value;

    if (!nome.trim()) {
        showNotification('O nome da marca não pode ser vazio.', 3000, 'error');
        return;
    }

    const marca = {
        id: id ? parseInt(id) : null,
        nome: nome
    };

    const method = id ? 'PUT' : 'POST';
    const url = id ? `${API_URL}/${id}` : API_URL;

    try {
        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(marca)
        });

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            throw new Error(errorData.message || 'Erro ao salvar a marca.');
        }

        showNotification('Marca salva com sucesso!', 3000, 'success');
        limparFormulario();
        carregarMarcas();

    } catch (error) {
        showNotification(error.message, 4000, 'error');
    }
}

async function editarMarca(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`);
        if (!response.ok) throw new Error('Marca não encontrada.');
        const marca = await response.json();

        marcaIdInput.value = marca.id;
        nomeInput.value = marca.nome;

        window.scrollTo({ top: 0, behavior: 'smooth' });
    } catch (error) {
        showNotification(`Erro ao carregar marca para edição: ${error.message}`, 3000, 'error');
    }
}

async function deletarMarca(id) {
    if (!id) return;

    try {
        const response = await fetch(`${API_URL}/${id}`, { method: 'DELETE' });

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            throw new Error(errorData.message || 'Erro ao deletar a marca.');
        }

        showNotification('Marca deletada com sucesso!', 3000, 'success');
        fecharModalConfirmacao();
        carregarMarcas();

    } catch (error) {
        showNotification(`Erro: ${error.message}`, 4000, 'error');
        fecharModalConfirmacao();
    }
}

// ==========================================================
// FUNÇÕES AUXILIARES (Modais, Formulário, Notificações)
// ==========================================================
function limparFormulario() {
    marcaForm.reset();
    marcaIdInput.value = '';
}

function abrirModalConfirmacao(id) {
    marcaIdToDelete = id;
    confirmModal.classList.remove('hidden');
}

function fecharModalConfirmacao() {
    marcaIdToDelete = null;
    confirmModal.classList.add('hidden');
}

function showNotification(message, duration = 3000, type = 'success') {
    if (!notification) return;
    notification.textContent = message;
    notification.className = `notification show ${type}`;
    setTimeout(() => {
        notification.className = 'notification hidden';
    }, duration);
}

