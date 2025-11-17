// ==========================================================
// CONFIGURAÇÃO DAS URLs DA API
// ==========================================================
const API_URL = 'http://localhost:8080/api/modelo';
const MARCA_API_URL = 'http://localhost:8080/api/marca';

// ==========================================================
// REFERÊNCIAS AOS ELEMENTOS DO DOM
// ==========================================================
const modeloForm = document.getElementById('modeloForm');
const modeloIdInput = document.getElementById('modeloId');
const nomeInput = document.getElementById('nome');
const marcaSelect = document.getElementById('marcaId'); // Corrigido de 'marca' para 'marcaId'
const modelosTableBody = document.querySelector('#modelosTable tbody');
const modeloCountBadge = document.getElementById('modeloCount');
const emptyState = document.getElementById('emptyState');
const confirmModal = document.getElementById('confirmModal');
const confirmYesBtn = document.getElementById('confirmYes');
const confirmNoBtn = document.getElementById('confirmNo');
const notification = document.getElementById('notification');

let modeloIdToDelete = null;

// ==========================================================
// INICIALIZAÇÃO E EVENTOS
// ==========================================================
document.addEventListener('DOMContentLoaded', () => {
    carregarMarcas();
    carregarModelos();
    modeloForm.addEventListener('submit', salvarModelo);
    confirmYesBtn.addEventListener('click', () => deletarModelo(modeloIdToDelete));
    confirmNoBtn.addEventListener('click', fecharModalConfirmacao);
});

// ==========================================================
// FUNÇÕES DE CARREGAMENTO DE DADOS
// ==========================================================
async function carregarMarcas() {
    try {
        const response = await fetch(MARCA_API_URL);
        if (!response.ok) throw new Error('Erro ao buscar marcas.');
        const marcas = await response.json();

        marcas.sort((a, b) => a.nome.localeCompare(b.nome));

        marcaSelect.innerHTML = '<option value="">Selecione uma marca</option>';
        marcas.forEach(marca => {
            marcaSelect.innerHTML += `<option value="${marca.id}">${marca.nome}</option>`;
        });
    } catch (error) {
        showNotification(`Erro ao carregar marcas: ${error.message}`, 3000, 'error');
    }
}

async function carregarModelos() {
    try {
        const response = await fetch(API_URL);
        if (!response.ok) throw new Error('Erro ao buscar modelos.');
        const modelos = await response.json();
        renderizarTabela(modelos);
    } catch (error) {
        showNotification(`Erro ao carregar modelos: ${error.message}`, 3000, 'error');
    }
}

// ==========================================================
// FUNÇÕES DE RENDERIZAÇÃO
// ==========================================================
function renderizarTabela(modelos) {
    modelosTableBody.innerHTML = '';
    emptyState.style.display = modelos.length === 0 ? 'block' : 'none';

    modelos.forEach(modelo => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${modelo.id}</td>
            <td>${modelo.nome}</td>
            <td>${modelo.marca ? modelo.marca.nome : 'N/A'}</td>
            <td class="actions">
                <button onclick="editarModelo(${modelo.id})" class="btn btn-sm btn-warning">
                    <i class="fas fa-edit"></i>
                </button>
                <button onclick="abrirModalConfirmacao(${modelo.id})" class="btn btn-sm btn-danger">
                    <i class="fas fa-trash"></i>
                </button>
            </td>
        `;
        modelosTableBody.appendChild(row);
    });

    modeloCountBadge.textContent = `${modelos.length} modelos`;
}

// ==========================================================
// FUNÇÕES DE CRUD
// ==========================================================
async function salvarModelo(event) {
    event.preventDefault();

    const id = modeloIdInput.value;
    
    // --- CORREÇÃO APLICADA AQUI ---
    // Monta o objeto no formato esperado pelo backend
    const modelo = {
        id: id ? parseInt(id) : null,
        nome: nomeInput.value,
        // O backend espera um objeto 'marca' com o 'id' dentro
        marca: { 
            id: parseInt(marcaSelect.value) 
        } 
    };

    // Validação para garantir que uma marca foi selecionada
    if (!modelo.marca.id) {
        showNotification('Por favor, selecione uma marca.', 3000, 'error');
        return;
    }
    
    const method = id ? 'PUT' : 'POST';
    // Usa a constante API_URL para garantir que a URL está correta
    const url = id ? `${API_URL}/${id}` : API_URL;

    try {
        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(modelo)
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Erro ao salvar modelo');
        }

        showNotification('Modelo salvo com sucesso!', 3000, 'success');
        limparFormulario();
        carregarModelos();
    } catch (error) {
        console.error(error);
        showNotification(error.message, 4000, 'error');
    }
}

async function editarModelo(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`);
        if (!response.ok) throw new Error('Modelo não encontrado.');
        const modelo = await response.json();

        modeloIdInput.value = modelo.id;
        nomeInput.value = modelo.nome;
        marcaSelect.value = modelo.marca.id;

        window.scrollTo({ top: 0, behavior: 'smooth' });
    } catch (error) {
        showNotification(`Erro: ${error.message}`, 3000, 'error');
    }
}

async function deletarModelo(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
        if (!response.ok) throw new Error('Erro ao deletar o modelo.');
        showNotification('Modelo deletado com sucesso!', 3000, 'success');
        fecharModalConfirmacao();
        carregarModelos();
    } catch (error) {
        showNotification(`Erro: ${error.message}`, 3000, 'error');
    }
}

// ==========================================================
// FUNÇÕES AUXILIARES
// ==========================================================
function limparFormulario() {
    modeloForm.reset();
    modeloIdInput.value = '';
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

function abrirModalConfirmacao(id) {
    modeloIdToDelete = id;
    confirmModal.classList.remove('hidden');
}

function fecharModalConfirmacao() {
    modeloIdToDelete = null;
    confirmModal.classList.add('hidden');
}
