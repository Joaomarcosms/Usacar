// ==========================================================
// CONFIGURAÇÃO DAS URLs DA API
// ==========================================================
const RELATORIO_COMISSAO_API_URL = 'http://localhost:8080/api/venda/comissoes';
const REAJUSTE_API_URL = 'http://localhost:8080/api/venda/comissoes/reajustar';
const RELATORIO_DETALHADO_API_URL = 'http://localhost:8080/api/relatorios/vendas';


document.addEventListener('DOMContentLoaded', () => {
    //formulário de relatório de comissões 
    const relatorioForm = document.getElementById('relatorioForm');
    if (relatorioForm) {
        relatorioForm.addEventListener('submit', gerarRelatorioComissao);
    }
    
    //botões de reajuste de comissão
    const simularBtn = document.getElementById('simularBtn');
    if (simularBtn) {
        simularBtn.addEventListener('click', () => enviarReajuste(true));
    }
    const confirmarBtn = document.getElementById('confirmarBtn');
    if (confirmarBtn) {
        confirmarBtn.addEventListener('click', () => enviarReajuste(false));
    }

    //formulário de relatório detalhado
    const relatorioDetalhadoForm = document.getElementById('relatorioDetalhadoForm');
    if (relatorioDetalhadoForm) {
        relatorioDetalhadoForm.addEventListener('submit', handleGerarRelatorioDetalhado);
    }
});


async function handleGerarRelatorioDetalhado(event) {
    event.preventDefault();
    const dataInicio = document.getElementById('dataInicioDetalhado').value;
    const dataFim = document.getElementById('dataFimDetalhado').value;

    if (!dataInicio || !dataFim) {
        showNotification("Por favor, selecione a data de início e a data final.", 4000, "error");
        return;
    }

    const loadingSpinner = document.getElementById('loadingSpinnerDetalhado');
    const relatorioContainer = document.getElementById('relatorioContainerDetalhado');
    const mensagemInfo = document.getElementById('mensagemInfoDetalhado');

    loadingSpinner.classList.remove('hidden');
    relatorioContainer.innerHTML = '';
    mensagemInfo.classList.add('hidden');

    try {
        const url = `${RELATORIO_DETALHADO_API_URL}?dataInicio=${dataInicio}&dataFim=${dataFim}`;
        const response = await fetch(url);
        if (!response.ok) throw new Error(`Erro ao buscar o relatório (Status: ${response.status})`);
        
        const relatorioData = await response.json();
        renderizarRelatorioDetalhado(relatorioData);
    } catch (error) {
        showNotification(error.message, 5000, "error");
        mensagemInfo.innerHTML = `<p>Ocorreu um erro ao gerar o relatório. Tente novamente.</p>`;
        mensagemInfo.classList.remove('hidden');
    } finally {
        loadingSpinner.classList.add('hidden');
    }
}

function renderizarRelatorioDetalhado(data) {
    const relatorioContainer = document.getElementById('relatorioContainerDetalhado');
    const mensagemInfo = document.getElementById('mensagemInfoDetalhado');

    if (!data || data.length === 0) {
        mensagemInfo.innerHTML = `<p>Nenhuma venda finalizada foi encontrada no período selecionado.</p>`;
        mensagemInfo.classList.remove('hidden');
        return;
    }

    data.forEach(item => {
        const card = document.createElement('div');
        card.className = 'relatorio-card glass-card';

        // Mapeia os detalhes de cada venda, formatando a data corretamente
        const vendasHtml = item.vendas.map(venda => `
            <li class="venda-detalhe">
                <span><strong>ID:</strong> ${venda.id}</span>
                <span><strong>Data:</strong> ${formatarDataArray(venda.data)}</span>
                <span><strong>Cliente:</strong> ${venda.cliente}</span>
                <span><strong>Veículo:</strong> ${venda.veiculo}</span>
                <span class="valor"><strong>Valor:</strong> ${formatarMoeda(venda.valor)}</span>
            </li>
        `).join('');

        card.innerHTML = `
            <div class="card-header vendedor-header">
                <div>
                    <h3 class="vendedor-nome">${item.vendedor.nome}</h3>
                    <p class="vendedor-cpf">CPF: ${item.vendedor.cpf}</p>
                </div>
                <div class="totais">
                    <span><strong>Total Vendas:</strong> ${item.quantidadeVendas}</span>
                    <span><strong>Valor Total:</strong> ${formatarMoeda(item.valorTotalVendido)}</span>
                </div>
            </div>
            <div class="card-content">
                <details>
                    <summary>Ver Detalhes das Vendas (${item.vendas.length})</summary>
                    <ul class="lista-vendas">${vendasHtml}</ul>
                </details>
            </div>
        `;
        relatorioContainer.appendChild(card);
    });
}



async function gerarRelatorioComissao(event) {
    event.preventDefault();
    const dataInicio = document.getElementById('dataInicioRelatorio').value;
    const dataFim = document.getElementById('dataFimRelatorio').value;
    const relatorioTableBody = document.querySelector('#relatorioTable tbody');

    if (!dataInicio) {
        showNotification('A data de início é obrigatória.', 3000, 'error');
        return;
    }

    const url = new URL(RELATORIO_COMISSAO_API_URL);
    url.searchParams.append('dataInicio', dataInicio);
    if (dataFim) {
        url.searchParams.append('dataFim', dataFim);
    }

    try {
        const response = await fetch(url);
        if (!response.ok) throw new Error('Erro ao gerar relatório de comissão.');
        
        const relatorio = await response.json();
        
        relatorioTableBody.innerHTML = '';
        if (relatorio.length === 0) {
            relatorioTableBody.innerHTML = '<tr><td colspan="4" class="text-center">Nenhum dado encontrado.</td></tr>';
            return;
        }
        relatorio.forEach(item => {
            relatorioTableBody.innerHTML += `
                <tr>
                    <td>${item.vendedor}</td>
                    <td>${item.quantidadeVendas ?? 0}</td>
                    <td>${formatarMoeda(item.valorTotalVendido ?? 0)}</td>
                    <td>${formatarMoeda(item.valorTotalComissao ?? item.comissaoRecebida ?? 0)}</td>
                </tr>
            `;
        });
    } catch (error) {
        showNotification(error.message, 3000, 'error');
    }
}

async function enviarReajuste(simulacao) {
    const dto = {
        dataInicio: document.getElementById('dataInicioReajuste').value,
        dataFim: document.getElementById('dataFimReajuste').value,
        valorMinimoTotalVendas: parseFloat(document.getElementById('valorMinimo').value),
        percentualReajuste: parseFloat(document.getElementById('percentual').value),
        simulacao: simulacao
    };

    if (!dto.dataInicio || !dto.dataFim || isNaN(dto.valorMinimoTotalVendas) || isNaN(dto.percentualReajuste)) {
        showNotification('Preencha todos os campos do reajuste.', 3000, 'error');
        return;
    }

    try {
        const response = await fetch(REAJUSTE_API_URL, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(dto),
        });

        if (!response.ok) throw new Error('Erro ao processar o reajuste.');
        
        const resultados = await response.json();
        renderizarResultadosReajuste(resultados, simulacao);

        const msg = simulacao ? `Simulação concluída: ${resultados.length} vendedores qualificados.` : `Reajuste confirmado para ${resultados.length} vendedores.`;
        showNotification(msg, 3000, 'success');
    } catch (error) {
        showNotification(error.message, 3000, 'error');
    }
}

function renderizarResultadosReajuste(resultados, simulacao) {
    const resultadosReajusteDiv = document.getElementById('resultadosReajuste');
    const resultadoTitulo = document.getElementById('resultadoTitulo');
    const reajusteTableBody = document.querySelector('#reajusteTable tbody');

    resultadosReajusteDiv.classList.remove('hidden');
    resultadoTitulo.textContent = simulacao ? 'Resultados da Simulação' : 'Resultados do Reajuste Aplicado';
    reajusteTableBody.innerHTML = '';

    if (resultados.length === 0) {
        reajusteTableBody.innerHTML = '<tr><td colspan="4" class="text-center">Nenhum vendedor se qualificou.</td></tr>';
        return;
    }

    resultados.forEach(item => {
        reajusteTableBody.innerHTML += `
            <tr>
                <td>${item.vendedor}</td>
                <td>${formatarMoeda(item.valorTotalVendas ?? 0)}</td>
                <td>${formatarMoeda(item.comissaoAnterior ?? 0)}</td>
                <td>${formatarMoeda(item.comissaoReajustada ?? 0)}</td>
            </tr>
        `;
    });
}


function formatarDataArray(dataArray) {
    if (!Array.isArray(dataArray) || dataArray.length < 3) {
        return 'Data inválida';
    }
    const [ano, mes, dia] = dataArray;
    const diaFormatado = dia.toString().padStart(2, '0');
    const mesFormatado = mes.toString().padStart(2, '0');
    return `${diaFormatado}/${mesFormatado}/${ano}`;
}

function formatarMoeda(valor) {
    if (typeof valor !== 'number') {
        valor = Number(valor) || 0;
    }
    return new Intl.NumberFormat('pt-BR', {
        style: 'currency',
        currency: 'BRL'
    }).format(valor);
}

function showNotification(message, duration = 3000, type = "success") {
    const notification = document.getElementById("notification");
    if (!notification) return;
    notification.textContent = message;
    notification.className = `notification show ${type}`;
    setTimeout(() => {
        notification.className = 'notification hidden';
    }, duration);
}
