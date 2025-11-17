// ==========================================================
// CONFIGURAÇÃO DA URL DA API
// ==========================================================
const apiUrl = 'http://localhost:8080/api/cliente';

// Variáveis de paginação
let clientesPaginados = [];
let paginaAtual = 1;
const clientesPorPagina = 10;

// Função para obter todos os clientes
async function fetchClientes() {
    try {
        const response = await fetch(`${apiUrl}`);
        if (!response.ok) throw new Error('Erro ao buscar clientes');
        const clientes = await response.json();
        renderClientes(clientes);
    } catch (error) {
        alert(error.message);
    }
}

// Função para renderizar os clientes com paginação
function renderClientes(clientes) {
    clientesPaginados = clientes;
    const tbody = document.querySelector('#clientesTable tbody');
    tbody.innerHTML = '';

    const inicio = (paginaAtual - 1) * clientesPorPagina;
    const fim = inicio + clientesPorPagina;
    const paginaClientes = clientes.slice(inicio, fim);

    paginaClientes.forEach(cliente => {
        tbody.innerHTML += `
            <tr>
                <td>${cliente.nome}</td>
                <td>${cliente.cpf}</td>
                <td>${cliente.email}</td>
                <td>${cliente.telefone}</td>
                <td>${new Date(cliente.dataCadastro).toLocaleDateString()}</td>
                <td>
                    <div class="actions">
                        <button class="btn btn-sm btn-warning glow-button" onclick="editCliente('${cliente.cpf}')">✏️ Editar</button>
                        <button class="btn btn-sm btn-danger glow-button" onclick="deleteCliente('${cliente.cpf}', '${cliente.nome}')">🗑️ Excluir</button>
                    </div>
                </td>
            </tr>
        `;
    });

    // Atualiza o contador de clientes
    document.getElementById("clienteCount").innerText = `${clientes.length} clientes`;

    renderPaginacao();
}

// Função para gerar os botões de paginação
function renderPaginacao() {
    const totalPaginas = Math.ceil(clientesPaginados.length / clientesPorPagina);
    const wrapper = document.querySelector('#clientesTable').parentElement;

    const antiga = document.getElementById('paginacao');
    if (antiga) wrapper.removeChild(antiga);

    if (totalPaginas <= 1) return;

    const paginacao = document.createElement('div');
    paginacao.id = 'paginacao';
    paginacao.classList.add('pagination');

    for (let i = 1; i <= totalPaginas; i++) {
        const btn = document.createElement('button');
        btn.textContent = i;
        btn.className = 'btn btn-sm ' + (i === paginaAtual ? 'btn-primary' : 'btn-outline');
        btn.addEventListener('click', () => {
            paginaAtual = i;
            renderClientes(clientesPaginados);
        });
        paginacao.appendChild(btn);
    }

    wrapper.appendChild(paginacao);
}

// Função para adicionar ou atualizar um cliente
async function saveCliente(event) {
    event.preventDefault();

    const id = document.getElementById('id').value.trim() || null;
    const method = id ? 'PUT' : 'POST';

    const cliente = {
        id,
        nome: document.getElementById('nome').value,
        cpf: document.getElementById('cpf').value,
        email: document.getElementById('email').value,
        dataCadastro: document.getElementById('dataCadastro').value,
        telefone: document.getElementById('telefone').value,
    };

    try {
        const response = await fetch(apiUrl, {
            method,
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(cliente),
        });

        if (!response.ok) throw new Error('Erro ao salvar o cliente');
        showNotification(id ? 'Atualização concluída com sucesso!' : 'Inserção concluída com sucesso!');
        clearForm();
        fetchClientes();
    } catch (error) {
        alert(error.message);
    }
}

// Função para pesquisar um cliente pelo CPF
async function searchCliente() {
    const cpf = document.getElementById('cpf').value;

    try {
        const response = await fetch(`${apiUrl}/obterporcpf/${cpf}`);
        if (!response.ok) throw new Error('Erro ao buscar cliente');
        const cliente = await response.json();

        if (cliente) {
            document.getElementById('id').value = cliente.id;
            document.getElementById('nome').value = cliente.nome;
            document.getElementById('cpf').value = cliente.cpf;
            document.getElementById('email').value = cliente.email;
            document.getElementById('dataCadastro').value =
                `${cliente.dataCadastro[0]}-${String(cliente.dataCadastro[1]).padStart(2, '0')}-${String(cliente.dataCadastro[2]).padStart(2, '0')}`;
            document.getElementById('telefone').value = cliente.telefone;
            showNotification('Cliente encontrado!');
        } else {
            clearForm();
            showNotification('Cliente não encontrado!');
        }
    } catch (error) {
        alert(error.message);
    }
}

// Função para limpar o formulário
function clearForm() {
    document.getElementById('clienteForm').reset();
    document.getElementById('id').value = "";
}

// Função para editar um cliente
async function editCliente(cpf) {
    try {
        const response = await fetch(`${apiUrl}/obterporcpf/${cpf}`);
        if (!response.ok) throw new Error('Erro ao buscar cliente');
        const cliente = await response.json();

        document.getElementById('id').value = cliente.id;
        document.getElementById('nome').value = cliente.nome;
        document.getElementById('cpf').value = cliente.cpf;
        document.getElementById('email').value = cliente.email;
        document.getElementById('telefone').value = cliente.telefone;
        document.getElementById('dataCadastro').value =
            `${cliente.dataCadastro[0]}-${String(cliente.dataCadastro[1]).padStart(2, '0')}-${String(cliente.dataCadastro[2]).padStart(2, '0')}`;
    } catch (error) {
        alert(error.message);
    }
}

// Função para deletar um cliente com modal funcional
function deleteCliente(cpf, nome) {
    const modal = document.getElementById('confirmModal');
    const message = modal.querySelector("p");
    message.textContent = `Tem certeza que deseja excluir o cliente ${nome}?`;
    modal.classList.remove('hidden');

    // Substituir listeners antigos
    const yesBtn = document.getElementById('confirmYes');
    const noBtn = document.getElementById('confirmNo');

    const novoYesBtn = yesBtn.cloneNode(true);
    const novoNoBtn = noBtn.cloneNode(true);

    yesBtn.parentNode.replaceChild(novoYesBtn, yesBtn);
    noBtn.parentNode.replaceChild(novoNoBtn, noBtn);

    // Evento de confirmação
    novoYesBtn.addEventListener('click', async () => {
        modal.classList.add('hidden');
        try {
            const response = await fetch(`${apiUrl}/deletar/${cpf}`, {
                method: 'DELETE'
            });

            if (!response.ok) throw new Error('Erro ao excluir o cliente');
            showNotification(`Cliente ${nome} excluído com sucesso!`);
            fetchClientes();
        } catch (error) {
            showNotification(error.message, 3000, 'error');
        }
    });

    // Evento de cancelamento
    novoNoBtn.addEventListener('click', () => {
        modal.classList.add('hidden');
    });
}

// Função para mostrar notificação
function showNotification(message, duration = 3000, type = 'success') {
    const notification = document.getElementById('notification');
    notification.textContent = message;

    notification.className = 'hidden';
    notification.classList.add(type);
    notification.classList.remove('hidden');
    notification.classList.add('show');

    setTimeout(() => {
        notification.classList.remove('show');
        notification.classList.add('hidden');
    }, duration);
}

// Inicialização
document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('clienteForm').addEventListener('submit', saveCliente);
    fetchClientes();
});

function showTab(tabId) {
    document.querySelectorAll('.tab-content').forEach(tab => {
        tab.classList.add('hidden');
    });

    document.querySelectorAll('.tab-button').forEach(btn => {
        btn.classList.remove('active');
    });

    document.getElementById(tabId).classList.remove('hidden');
    document.querySelector(`[onclick="showTab('${tabId}')"]`).classList.add('active');
}
