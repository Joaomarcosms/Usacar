// ==========================================================
// CONFIGURAÇÃO DAS URLs DA API
// ==========================================================
const apiUrlBase = 'http://localhost:8080/api/carro';
const STATUS_API_URL = 'http://localhost:8080/api/status';
const MARCA_API_URL = 'http://localhost:8080/api/marca';
const MODELO_API_URL = 'http://localhost:8080/api/modelo';
const COR_API_URL = 'http://localhost:8080/api/cor';


let carrosPaginados = [];
let paginaAtualCarros = 1;
const carrosPorPagina = 10;
let idCarroParaExcluir = null;


document.addEventListener("DOMContentLoaded", () => {
    console.log("Página carregada. Iniciando aplicação.");
    vincularEventos();
    carregarDadosIniciais();
});


function vincularEventos() {
    const carroForm = document.getElementById('carroForm');
    if (carroForm) {
        carroForm.addEventListener('submit', saveCarro);
    }

    const marcaSelect = document.getElementById('marcaId');
    if (marcaSelect) {
        marcaSelect.addEventListener('change', carregarModelos);
    }

    const saveStatusBtn = document.getElementById('saveStatusBtn');
    if (saveStatusBtn) {
        saveStatusBtn.addEventListener('click', updateCarStatus);
    }

    const cancelStatusBtn = document.getElementById('cancelStatusBtn');
    if (cancelStatusBtn) {
        const updateStatusModal = document.getElementById('updateStatusModal');
        cancelStatusBtn.addEventListener('click', () => {
            if (updateStatusModal) updateStatusModal.classList.add('hidden');
        });
    }

    const closeHistoricoBtn = document.getElementById('closeHistoricoBtn');
    if (closeHistoricoBtn) {
        const historicoModal = document.getElementById('historicoModal');
        closeHistoricoBtn.addEventListener('click', () => {
            if (historicoModal) historicoModal.classList.add('hidden');
        });
    }
}


async function carregarDadosIniciais() {
    await Promise.all([
        carregarCarros(),
        carregarMarcas(),
        carregarCores(),
        carregarStatus()
    ]);
}

async function carregarCarros() {
    const tabela = document.getElementById('carrosTable');
    if (!tabela) return;

    try {
        const response = await fetch(apiUrlBase);
        if (!response.ok) throw new Error('Erro ao buscar os carros.');
        carrosPaginados = await response.json();
        renderizarTabela(carrosPaginados);
    } catch (error) {
        console.error("Falha ao carregar Carros:", error);
        showNotification("Não foi possível carregar a lista de carros.", 4000, "error");
    }
}

async function carregarMarcas() {
    const marcaSelect = document.getElementById('marcaId');
    if (!marcaSelect) return;

    try {
        const response = await fetch(MARCA_API_URL);
        if (!response.ok) throw new Error('API de Marcas falhou.');
        const marcas = await response.json();
        marcaSelect.innerHTML = '<option value="">Selecione uma marca</option>';
        marcas.forEach(marca => {
            marcaSelect.innerHTML += `<option value="${marca.id}">${marca.nome}</option>`;
        });
    } catch (error) {
        console.error("Falha ao carregar Marcas:", error);
    }
}

async function carregarModelos() {
    const marcaSelect = document.getElementById('marcaId');
    const modeloSelect = document.getElementById('modeloId');
    if (!marcaSelect || !modeloSelect) return;

    const marcaId = marcaSelect.value;
    modeloSelect.innerHTML = '<option value="">Carregando...</option>';

    if (!marcaId) {
        modeloSelect.innerHTML = '<option value="">Selecione uma marca primeiro</option>';
        return;
    }

    try {
        const response = await fetch(`${MODELO_API_URL}?marcaId=${marcaId}`);
        if (!response.ok) throw new Error(`API de Modelos falhou`);
        const modelos = await response.json();
        modeloSelect.innerHTML = '<option value="">Selecione um modelo</option>';
        if (modelos.length === 0) {
            modeloSelect.innerHTML = '<option value="">Nenhum modelo encontrado</option>';
        } else {
            modelos.forEach(modelo => {
                modeloSelect.innerHTML += `<option value="${modelo.id}">${modelo.nome}</option>`;
            });
        }
    } catch (error) {
        console.error("Falha ao carregar Modelos:", error);
        modeloSelect.innerHTML = '<option value="">Erro ao carregar</option>';
    }
}

async function carregarCores() {
    const corSelect = document.getElementById('corId');
    if (!corSelect) return;

    try {
        const response = await fetch(COR_API_URL);
        if (!response.ok) throw new Error('API de Cores falhou.');
        const cores = await response.json();
        corSelect.innerHTML = '<option value="">Selecione uma cor</option>';
        cores.forEach(cor => {
            corSelect.innerHTML += `<option value="${cor.id}">${cor.nome}</option>`;
        });
    } catch (error) {
        console.error("Falha ao carregar Cores:", error);
    }
}

async function carregarStatus() {
    const statusSelect = document.getElementById('statusId');
    const novoStatusSelect = document.getElementById('novoStatusId');
    if (!statusSelect) return;

    try {
        const response = await fetch(STATUS_API_URL);
        if (!response.ok) throw new Error('API de Status falhou.');
        const todosStatus = await response.json();
        statusSelect.innerHTML = '<option value="">Selecione um status</option>';
        if (novoStatusSelect) {
            novoStatusSelect.innerHTML = '<option value="">Selecione um novo status</option>';
        }
        todosStatus.forEach(status => {
            statusSelect.innerHTML += `<option value="${status.id}">${status.descricao}</option>`;
            if (novoStatusSelect) {
                novoStatusSelect.innerHTML += `<option value="${status.id}">${status.descricao}</option>`;
            }
        });
    } catch (error) {
        console.error("Falha ao carregar Status:", error);
    }
}



function renderizarTabela(carros) {
    const tabelaBody = document.querySelector("#carrosTable tbody");
    if (!tabelaBody) return;

    tabelaBody.innerHTML = "";
    
    const emptyState = document.getElementById("emptyState");
    if(emptyState) emptyState.style.display = carros.length === 0 ? "block" : "none";

    const inicio = (paginaAtualCarros - 1) * carrosPorPagina;
    const fim = inicio + carrosPorPagina;
    const paginaCarros = carros.slice(inicio, fim);

    paginaCarros.forEach((carro) => {
        const row = document.createElement("tr");
        row.innerHTML = `
          <td>${carro.placa || 'N/A'}</td>
          <td>${carro.modelo?.marca?.nome || 'N/A'}</td>
          <td>${carro.modelo?.nome || 'N/A'}</td>
          <td>${carro.anoFabricacao || 'N/A'}</td>
          <td>${carro.cor?.nome || 'N/A'}</td>
          <td>${carro.quilometragem || 'N/A'}</td>
          <td>${carro.status?.descricao || 'N/A'}</td>
          <td>
            <div class="actions">
              <button class="btn btn-sm btn-secondary" onclick="abrirHistoricoModal(${carro.id})"><i class="fas fa-history"></i></button>
              <button class="btn btn-sm btn-warning" onclick="abrirModalEdicao(${carro.id})"><i class="fas fa-edit"></i></button>
              <button class="btn btn-sm btn-danger" onclick="excluirCarroModal(${carro.id})"><i class="fas fa-trash"></i></button>
            </div>
          </td>
        `;
        tabelaBody.appendChild(row);
    });

    const carroCount = document.getElementById("carroCount");
    if (carroCount) carroCount.innerText = `${carros.length} carros`;
    
    renderPaginacaoCarros(carros.length);
}

function renderPaginacaoCarros(totalCarros) {
    const paginacaoContainer = document.getElementById('pagination');
    if (!paginacaoContainer) return;
    paginacaoContainer.innerHTML = '';
    const totalPaginas = Math.ceil(totalCarros / carrosPorPagina);
    if (totalPaginas <= 1) return;
    for (let i = 1; i <= totalPaginas; i++) {
        const btn = document.createElement('button');
        btn.textContent = i;
        btn.className = 'btn btn-sm ' + (i === paginaAtualCarros ? 'btn-primary' : 'btn-outline');
        btn.onclick = () => {
            paginaAtualCarros = i;
            renderizarTabela(carrosPaginados);
        };
        paginacaoContainer.appendChild(btn);
    }
}


async function saveCarro(event) {
    event.preventDefault();
    const idInput = document.getElementById('carroId');
    const id = idInput ? idInput.value : null;
    const method = id ? 'PUT' : 'POST';
    const url = id ? `${apiUrlBase}/${id}` : apiUrlBase;

    const carro = {
        id: id ? parseInt(id) : null,
        placa: document.getElementById('placa').value,
        anoFabricacao: parseInt(document.getElementById('anoFabricacao').value),
        quilometragem: parseInt(document.getElementById('km').value),
        anoModelo: parseInt(document.getElementById('anoModelo').value),
        precoCompra: parseFloat(document.getElementById('precoCompra').value),
        dataCadastro: new Date().toISOString().split('T')[0],
        modelo: { id: parseInt(document.getElementById('modeloId').value) },
        cor: { id: parseInt(document.getElementById('corId').value) },
        status: { id: parseInt(document.getElementById('statusId').value) }
    };

    try {
        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(carro),
        });
        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            throw new Error(errorData.message || 'Falha ao salvar o carro.');
        }
        showNotification(id ? 'Carro atualizado!' : 'Carro salvo!', 3000, 'success');
        limparFormulario();
        carregarCarros();
    } catch (error) {
        showNotification(error.message, 5000, 'error');
    }
}

async function abrirModalEdicao(carroId) {
    try {
        const response = await fetch(`${apiUrlBase}/${carroId}`);
        if (!response.ok) throw new Error('Carro não encontrado para edição.');
        const carro = await response.json();

        document.getElementById('carroId').value = carro.id;
        document.getElementById('placa').value = carro.placa;
        document.getElementById('anoFabricacao').value = carro.anoFabricacao;
        document.getElementById('km').value = carro.quilometragem;
        document.getElementById('anoModelo').value = carro.anoModelo;
        document.getElementById('precoCompra').value = carro.precoCompra;
        
        const marcaSelect = document.getElementById('marcaId');
        marcaSelect.value = carro.modelo.marca.id;
        
        await carregarModelos();
        
        document.getElementById('modeloId').value = carro.modelo.id;
        document.getElementById('corId').value = carro.cor.id;
        document.getElementById('statusId').value = carro.status.id;

        window.scrollTo({ top: 0, behavior: 'smooth' });
    } catch (error) {
        showNotification(error.message, 4000, 'error');
    }
}

function excluirCarroModal(carroId) {
    idCarroParaExcluir = carroId;
    const confirmacao = confirm(`Tem certeza que deseja excluir o carro com ID ${carroId}?`);
    if (confirmacao) {
        deletarCarro(idCarroParaExcluir);
    }
}

async function deletarCarro(id) {
    try {
        const response = await fetch(`${apiUrlBase}/${id}`, { method: 'DELETE' });
        if (!response.ok) throw new Error('Erro ao excluir carro.');
        showNotification('Carro excluído com sucesso!');
        carregarCarros();
    } catch (error) {
        showNotification(error.message, 4000, 'error');
    }
}

async function abrirHistoricoModal(carroId) {
    const historicoModal = document.getElementById('historicoModal');
    const historicoTableBody = document.getElementById('historicoTableBody');

    if (!historicoModal || !historicoTableBody) {
        console.error("Elementos do modal de histórico não encontrados!");
        return;
    }

    historicoTableBody.innerHTML = '<tr><td colspan="4" class="text-center">Carregando histórico...</td></tr>';
    historicoModal.classList.remove('hidden');

    try {
        const response = await fetch(`${apiUrlBase}/${carroId}/historico-status`);
        if (!response.ok) {
            throw new Error('Não foi possível carregar o histórico do carro.');
        }
        const historico = await response.json();
        renderizarHistorico(historico);
    } catch (error) {
        historicoTableBody.innerHTML = `<tr><td colspan="4" class="text-center">${error.message}</td></tr>`;
    }
}

function renderizarHistorico(historico) {
    const historicoTableBody = document.getElementById('historicoTableBody');
    if (!historicoTableBody) return;

    historicoTableBody.innerHTML = '';

    if (historico.length === 0) {
        historicoTableBody.innerHTML = '<tr><td colspan="4" class="text-center">Nenhum histórico encontrado para este carro.</td></tr>';
        return;
    }

    historico.forEach(log => {
        const row = document.createElement('tr');
        const dataFormatada = new Date(log.dataHora).toLocaleString('pt-BR', {
            day: '2-digit', month: '2-digit', year: 'numeric',
            hour: '2-digit', minute: '2-digit'
        });

        row.innerHTML = `
          <td>${dataFormatada}</td>
          <td>${log.statusAnterior || 'N/A'}</td>
          <td>${log.novoStatus}</td>
          <td>${log.usuarioResponsavel}</td>
        `;
        historicoTableBody.appendChild(row);
    });
}


function limparFormulario() {
    const carroForm = document.getElementById('carroForm');
    if (carroForm) carroForm.reset();
    const idInput = document.getElementById('carroId');
    if (idInput) idInput.value = '';
    const modeloSelect = document.getElementById('modeloId');
    if (modeloSelect) modeloSelect.innerHTML = '<option value="">Selecione uma marca primeiro</option>';
}

function showNotification(message, duration = 3000, type = "success") {
    const notification = document.getElementById("notification");
    if (!notification) return;
    notification.textContent = message;
    notification.className = "notification " + type;
    notification.classList.remove("hidden");
    setTimeout(() => {
        notification.classList.add("hidden");
    }, duration);
}
