// ==========================================================
// CONFIGURAÇÃO DA URL DA API
// ==========================================================
const API_URL = "http://localhost:8080/api/venda/relatorios/marcas-mais-vendidas";

// Referências aos elementos HTML
const rankingFilterForm = document.getElementById('rankingFilterForm');
const dataInicioInput = document.getElementById('dataInicio');
const dataFimInput = document.getElementById('dataFim');
const rankingTableBody = document.querySelector('#rankingTable tbody');
const rankingCountBadge = document.getElementById('rankingCount');
const rankingChartCanvas = document.getElementById('rankingChart');
const chartEmptyState = document.getElementById('chartEmptyState');
const tableEmptyState = document.getElementById('tableEmptyState');

let myChart = null; 

// Registrar o plugin datalabels
Chart.register(ChartDataLabels);

// Inicialização: Carrega o ranking ao carregar a página
document.addEventListener("DOMContentLoaded", () => {
    fetchRankingData();
    rankingFilterForm.addEventListener('submit', (event) => {
        event.preventDefault(); // Evita o recarregamento da página
        fetchRankingData();
    });
});

// Função para buscar os dados do ranking da API
async function fetchRankingData() {
    const dataInicio = dataInicioInput.value;
    const dataFim = dataFimInput.value;

    const params = new URLSearchParams();
    if (dataInicio) params.append('dataInicio', dataInicio);
    if (dataFim) params.append('dataFim', dataFim);

    try {
        const response = await fetch(`${API_URL}?${params.toString()}`);
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.mensagem || 'Erro ao buscar o ranking de marcas.');
        }
        const rankingData = await response.json();
        console.log("Dados do Ranking recebidos:", rankingData);
        renderRankingTable(rankingData);
        renderRankingChart(rankingData);
        showNotification('Ranking atualizado com sucesso!', 3000, 'success');
    } catch (error) {
        console.error("Erro ao buscar ranking:", error);
        showNotification(error.message, 4000, 'error');
        renderRankingTable([]); // Limpa a tabela em caso de erro
        renderRankingChart([]); // Limpa o gráfico em caso de erro
    }
}

// Função para renderizar a tabela de ranking
function renderRankingTable(data) {
    rankingTableBody.innerHTML = ''; // Limpa o corpo da tabela

    if (data.length === 0) {
        tableEmptyState.classList.remove('hidden');
        rankingCountBadge.textContent = '0 Marcas';
        return;
    } else {
        tableEmptyState.classList.add('hidden');
    }

    data.forEach(item => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${item.marca}</td>
            <td>${item.quantidadeVendida}</td>
            <td>${item.valorTotalVendido.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })}</td>
            <td>${item.ticketMedio.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })}</td>
        `;
        rankingTableBody.appendChild(row);
    });
    rankingCountBadge.textContent = `${data.length} Marcas`;
}

// Função para renderizar o gráfico de ranking
function renderRankingChart(data) {
    // Destrói o gráfico existente se houver um
    if (myChart) {
        myChart.destroy();
    }

    if (data.length === 0) {
        rankingChartCanvas.classList.add('hidden');
        chartEmptyState.classList.remove('hidden');
        return;
    } else {
        rankingChartCanvas.classList.remove('hidden');
        chartEmptyState.classList.add('hidden');
    }

    const labels = data.map(item => item.marca);
    const quantidadeVendidaData = data.map(item => item.quantidadeVendida);
    const valorTotalVendidoData = data.map(item => item.valorTotalVendido);

    const ctx = rankingChartCanvas.getContext('2d');
    myChart = new Chart(ctx, {
        type: 'bar', // Tipo de gráfico
        data: {
            labels: labels,
            datasets: [
                {
                    label: 'Quantidade Vendida',
                    data: quantidadeVendidaData,
                    backgroundColor: 'rgba(54, 162, 235, 0.7)', 
                    borderColor: 'rgba(54, 162, 235, 1)',
                    borderWidth: 1,
                    datalabels: { 
                        align: 'end',
                        anchor: 'end',
                        color: '#f8f8f8', 
                        font: {
                            weight: 'bold'
                        },
                        formatter: function(value) {
                            return value; 
                        }
                    }
                },
                {
                    label: 'Valor Total Vendido (R$)',
                    data: valorTotalVendidoData,
                    backgroundColor: 'rgba(255, 159, 64, 0.7)', 
                    borderColor: 'rgba(255, 159, 64, 1)',
                    borderWidth: 1,
                    datalabels: { 
                        align: 'end',
                        anchor: 'end',
                        color: '#f8f8f8', 
                        font: {
                            weight: 'bold'
                        },
                        formatter: function(value) {
                            return value.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL', minimumFractionDigits: 0, maximumFractionDigits: 0 }); // Formata como moeda sem centavos
                        }
                    }
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false, 
            plugins: {
                legend: {
                    labels: {
                        color: '#f8f8f8' // Cor do texto da legenda
                    }
                },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            let label = context.dataset.label || '';
                            if (label) {
                                label += ': ';
                            }
                            if (context.dataset.label === 'Valor Total Vendido (R$)') {
                                label += context.raw.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
                            } else {
                                label += context.raw;
                            }
                            return label;
                        }
                    }
                },
                datalabels: { 
                    display: true, 
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: 'Valores',
                        color: '#f8f8f8' 
                    },
                    ticks: {
                        color: '#b0b0b0' 
                    },
                    grid: {
                        color: 'rgba(255, 255, 255, 0.1)' 
                    }
                },
                x: {
                    title: {
                        display: true,
                        text: 'Marca',
                        color: '#f8f8f8'
                    },
                    ticks: {
                        color: '#b0b0b0' 
                    },
                    grid: {
                        color: 'rgba(255, 255, 255, 0.1)'
                    }
                }
            }
        }
    });
}

// Função para limpar os campos de filtro e recarregar o ranking
function limparFiltro() {
    dataInicioInput.value = '';
    dataFimInput.value = '';
    fetchRankingData();
}

// Função de notificação
function showNotification(message, duration = 3000, type = "success") {
    const notification = document.getElementById("notification");
    notification.textContent = message;
    notification.className = "notification " + type; 
    notification.classList.remove("hidden");

    setTimeout(() => {
        notification.classList.add("hidden");
    }, duration);
}
