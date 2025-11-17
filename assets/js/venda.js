// ==========================================================
// CONFIGURAÇÃO DAS URLs DA API
// ==========================================================
const API_URL = 'http://localhost:8080/api/venda';
const CARRO_API_URL = 'http://localhost:8080/api/carro';
const CLIENTE_API_URL = 'http://localhost:8080/api/cliente';
const VENDEDOR_API_URL = 'http://localhost:8080/api/vendedores'; 

// ==========================================================
// REFERÊNCIAS AOS ELEMENTOS DO DOM
// ==========================================================
const vendaForm = document.getElementById('vendaForm');
const idInput = document.getElementById('id');
const dataVendaInput = document.getElementById('dataVenda');
const valorVendaInput = document.getElementById('valorVenda');
const valorComissaoInput = document.getElementById('valorComissao');
const carroSelect = document.getElementById('carroId');
const clienteSelect = document.getElementById('clienteId');
const vendedorSelect = document.getElementById('vendedorId');
const tabelaVendas = document.getElementById('tabelaVendas');
const vendaCountBadge = document.getElementById('vendaCount');
const emptyState = document.getElementById('emptyState');
const confirmModal = document.getElementById('confirmModal');
const confirmYesBtn = document.getElementById('confirmYes');
const confirmNoBtn = document.getElementById('confirmNo');
const cancelarVendaModal = document.getElementById('cancelarVendaModal');
const confirmarCancelamentoBtn = document.getElementById('confirmarCancelamentoBtn');
const cancelarCancelamentoBtn = document.getElementById('cancelarCancelamentoBtn');

// Variáveis de estado
let allVendas = [];
let paginaAtual = 1;
const vendasPorPagina = 10;
let vendaIdToDelete = null;
let vendaIdToCancel = null;

// ==========================================================
// INICIALIZAÇÃO E EVENTOS
// ==========================================================
document.addEventListener('DOMContentLoaded', () => {
    carregarDados();
    vendaForm.addEventListener('submit', salvarVenda);
    confirmYesBtn.addEventListener('click', () => deletarVenda(vendaIdToDelete));
    confirmNoBtn.addEventListener('click', fecharModalConfirmacao);
    confirmarCancelamentoBtn.addEventListener('click', () => cancelarVenda(vendaIdToCancel));
    cancelarCancelamentoBtn.addEventListener('click', () => cancelarVendaModal.classList.add('hidden'));
});

// ==========================================================
// FUNÇÕES DE CARREGAMENTO DE DADOS
// ==========================================================

async function carregarDados() {
    // Executa todas as chamadas em paralelo para carregar a página mais rápido
    await Promise.all([
        carregarCarros(),
        carregarClientes(),
        carregarVendedores(),
        carregarVendas()
    ]);
}

async function carregarCarros() {
    try {
        const response = await fetch(CARRO_API_URL);
        if (!response.ok) throw new Error('Erro ao buscar carros.');
        const carros = await response.json();
        carroSelect.innerHTML = '<option value="">Selecione um carro</option>';
        // Filtra para mostrar apenas carros com status "Disponível"
        const carrosDisponiveis = carros.filter(carro => carro.status && carro.status.descricao === 'Disponível');
        carrosDisponiveis.sort((a, b) => a.modelo.nome.localeCompare(b.modelo.nome));
        carrosDisponiveis.forEach(carro => {
            const optionText = `${carro.modelo.marca.nome} ${carro.modelo.nome} (${carro.placa})`;
            carroSelect.innerHTML += `<option value="${carro.id}">${optionText}</option>`;
        });
    } catch (error) {
        showNotification(`Erro ao carregar carros: ${error.message}`, 3000, 'error');
    }
}

async function carregarClientes() {
    try {
        const response = await fetch(CLIENTE_API_URL);
        if (!response.ok) throw new Error('Erro ao buscar clientes.');
        const clientes = await response.json();
        clienteSelect.innerHTML = '<option value="">Selecione um cliente</option>';
        clientes.sort((a, b) => a.nome.localeCompare(b.nome));
        clientes.forEach(cliente => {
            clienteSelect.innerHTML += `<option value="${cliente.id}">${cliente.nome}</option>`;
        });
    } catch (error) {
        showNotification(`Erro ao carregar clientes: ${error.message}`, 3000, 'error');
    }
}

async function carregarVendedores() {
    try {
        const response = await fetch(VENDEDOR_API_URL);
        if (!response.ok) throw new Error('Erro ao buscar vendedores.');
        const vendedores = await response.json();
        vendedorSelect.innerHTML = '<option value="">Selecione um vendedor</option>';
        vendedores.sort((a, b) => a.nome.localeCompare(b.nome));
        vendedores.forEach(vendedor => {
            vendedorSelect.innerHTML += `<option value="${vendedor.id}">${vendedor.nome}</option>`;
        });
    } catch (error) {
        showNotification(`Erro ao carregar vendedores: ${error.message}`, 3000, 'error');
    }
}

async function carregarVendas() {
    try {
        const response = await fetch(API_URL);
        if (!response.ok) throw new Error('Erro ao carregar vendas.');
        allVendas = await response.json();
        vendaCountBadge.textContent = `${allVendas.length} vendas`;
        renderizarVendas();
    } catch (error) {
        showNotification(`Erro ao carregar vendas: ${error.message}`, 3000, 'error');
    }
}

// ==========================================================
// FUNÇÕES DE RENDERIZAÇÃO
// ==========================================================

function renderizarVendas() {
    tabelaVendas.innerHTML = '';
    emptyState.style.display = allVendas.length === 0 ? 'block' : 'none';

    const inicio = (paginaAtual - 1) * vendasPorPagina;
    const fim = inicio + vendasPorPagina;
    const vendasDaPagina = allVendas.slice(inicio, fim);

    vendasDaPagina.forEach(venda => {
        const row = document.createElement('tr');
        
        // --- CORREÇÃO APLICADA AQUI ---
        // Agora, exibe os nomes dos objetos aninhados, com verificação para evitar erros.
        const carroDesc = venda.carro ? `${venda.carro.modelo.marca.nome} ${venda.carro.modelo.nome}` : 'N/A';
        const clienteNome = venda.cliente ? venda.cliente.nome : 'N/A';
        const vendedorNome = venda.vendedor ? venda.vendedor.nome : 'N/A';

        row.innerHTML = `
            <td>${venda.id}</td>
            <td>${formatarData(venda.dataVenda)}</td>
            <td>${formatarMoeda(venda.valorVenda)}</td>
            <td>${formatarMoeda(venda.valorComissao)}</td>
            <td>${carroDesc}</td>
            <td>${clienteNome}</td>
            <td>${vendedorNome}</td>
            <td class="actions">
                <button onclick="editarVenda(${venda.id})" class="btn btn-sm btn-warning"><i class="fas fa-edit"></i></button>
                <button onclick="abrirModalCancelamento(${venda.id})" class="btn btn-sm btn-secondary"><i class="fas fa-ban"></i></button>
                <button onclick="abrirModalConfirmacao(${venda.id})" class="btn btn-sm btn-danger"><i class="fas fa-trash"></i></button>
            </td>
        `;
        tabelaVendas.appendChild(row);
    });

    renderizarPaginacao();
}

function renderizarPaginacao() {
    const paginationDiv = document.getElementById('pagination');
    if (!paginationDiv) return;
    
    paginationDiv.innerHTML = '';
    const totalPaginas = Math.ceil(allVendas.length / vendasPorPagina);

    if (totalPaginas <= 1) return;

    for (let i = 1; i <= totalPaginas; i++) {
        const btn = document.createElement('button');
        btn.textContent = i;
        btn.className = `btn btn-sm ${i === paginaAtual ? 'btn-primary' : 'btn-outline'}`;
        btn.addEventListener('click', () => {
            paginaAtual = i;
            renderizarVendas();
        });
        paginationDiv.appendChild(btn);
    }
}

// ==========================================================
// FUNÇÕES DE CRUD E AÇÕES
// ==========================================================

async function salvarVenda(event) {
    event.preventDefault();
    const id = idInput.value;
    // Ao salvar, enviamos o DTO simples com os IDs
    const vendaDTO = {
        id: id ? parseInt(id) : null,
        dataVenda: dataVendaInput.value,
        valorVenda: parseFloat(valorVendaInput.value),
        valorComissao: parseFloat(valorComissaoInput.value),
        carroId: parseInt(carroSelect.value),
        clienteId: parseInt(clienteSelect.value),
        vendedorId: parseInt(vendedorSelect.value)
    };
    const method = id ? 'PUT' : 'POST';
    const url = id ? `${API_URL}/${id}` : API_URL;

    try {
        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(vendaDTO) // Envia o DTO com IDs
        });
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Erro ao salvar a venda.');
        }
        showNotification('Venda salva com sucesso!', 3000, 'success');
        limparForm();
        carregarDados();
    } catch (error) {
        showNotification(`Erro: ${error.message}`, 3000, 'error');
    }
}

async function editarVenda(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`);
        if (!response.ok) throw new Error('Venda não encontrada.');
        const venda = await response.json(); // Recebe o VendaResponseDTO completo
        idInput.value = venda.id;
        // A API retorna a data como um array [ano, mes, dia], precisamos formatá-la
        dataVendaInput.value = venda.dataVenda.join('-').padStart(2, '0');
        valorVendaInput.value = venda.valorVenda;
        valorComissaoInput.value = venda.valorComissao;
        // Preenche os selects com os IDs dos objetos recebidos
        carroSelect.value = venda.carro.id;
        clienteSelect.value = venda.cliente.id;
        vendedorSelect.value = venda.vendedor.id;
        window.scrollTo({ top: 0, behavior: 'smooth' }); 
    } catch (error) {
        showNotification(`Erro: ${error.message}`, 3000, 'error');
    }
}

async function deletarVenda(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
        if (!response.ok) throw new Error('Erro ao deletar a venda.');
        showNotification('Venda deletada com sucesso!', 3000, 'success');
        fecharModalConfirmacao();
        carregarDados();
    } catch (error) {
        showNotification(`Erro: ${error.message}`, 3000, 'error');
    }
}

function abrirModalCancelamento(id) {
    vendaIdToCancel = id;
    document.getElementById('cancelarVendaMessage').textContent = `Tem certeza que deseja cancelar a venda #${id}? Esta ação não pode ser desfeita.`;
    cancelarVendaModal.classList.remove('hidden');
}

async function cancelarVenda(id) {
    try {
        const response = await fetch(`${API_URL}/${id}/cancelar`, { method: 'POST' });
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Não foi possível cancelar a venda.');
        }
        const result = await response.json();
        showNotification(`Venda #${id} cancelada. Status do carro #${result.carroId} restaurado.`, 4000, 'success');
        cancelarVendaModal.classList.add('hidden');
        carregarDados();
    } catch (error) {
        showNotification(`Erro: ${error.message}`, 3000, 'error');
        cancelarVendaModal.classList.add('hidden');
    }
}

// ==========================================================
// FUNÇÕES AUXILIARES
// ==========================================================

function limparForm() {
    vendaForm.reset();
    idInput.value = '';
}

function abrirModalConfirmacao(id) {
    vendaIdToDelete = id;
    confirmModal.classList.remove('hidden');
}

function fecharModalConfirmacao() {
    vendaIdToDelete = null;
    confirmModal.classList.add('hidden');
}

function formatarData(dataArray) {
    if (!Array.isArray(dataArray) || dataArray.length < 3) return 'N/A';
    const [ano, mes, dia] = dataArray;
    return `${dia.toString().padStart(2, '0')}/${mes.toString().padStart(2, '0')}/${ano}`;
}

function formatarMoeda(valor) {
    if (typeof valor !== 'number') return 'R$ 0,00';
    return valor.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
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
