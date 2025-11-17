document.addEventListener('DOMContentLoaded', () => {
    const historicoForm = document.getElementById('historicoForm');
    if (historicoForm) {
        historicoForm.addEventListener('submit', buscarHistorico);
    }
});


// Função para buscar o histórico de vendas do cliente via API
async function buscarHistorico(event) {
    event.preventDefault();

    const cpfInput = document.getElementById('clienteCpf');
    
    // Remove todos os pontos, traços e outros caracteres, deixando apenas os números.
    const cpf = cpfInput.value.replace(/\D/g, '');

    // Validação para garantir que o CPF tem 11 dígitos
    if (!cpf || cpf.length !== 11) {
        showNotification('Por favor, informe um CPF válido com 11 dígitos.', 3000, 'error');
        return;
    }

    console.log(`Buscando histórico para o CPF (apenas números): ${cpf}`);

    const url = `http://localhost:8080/api/cliente/cpf/${cpf}/historico-vendas`;

    try {
        const response = await fetch(url);

        if (response.status === 404) {
            throw new Error('Cliente com este CPF não foi encontrado.');
        }
        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            throw new Error(errorData.message || 'Erro ao buscar histórico de vendas.');
        }

        const dados = await response.json();
        renderHistorico(dados);
       

    } catch (error) {
        showNotification(error.message, 4000, 'error');
        limparTabela();
    }
}


// Função para renderizar os dados na tabela
function renderHistorico(historico) {
    const tbody = document.querySelector('#historicoTable tbody');
    tbody.innerHTML = '';

    if (historico.length === 0) {
        tbody.innerHTML = `<tr><td colspan="10" style="text-align: center;">Nenhuma venda encontrada para este cliente.</td></tr>`;
        return;
    }

    historico.forEach(item => {
        tbody.innerHTML += `
            <tr>
                <td>${formatarDataVenda(item.dataVenda)}</td>
                <td>R$ ${Number(item.valorVenda).toFixed(2)}</td>
                <td>R$ ${Number(item.valorComissao).toFixed(2)}</td>
                <td>${capitalize(item.carro.marca)}</td>
                <td>${capitalize(item.carro.modelo)}</td>
                <td>${item.carro.anoFabricacao}</td>
                <td>${item.carro.anoModelo}</td>
                <td>${item.carro.placa.toUpperCase()}</td>
                <td>${capitalize(item.carro.cor)}</td>
                <td>${item.vendedor}</td>
            </tr>
        `;
    });
}

// Função para formatar a data que vem como array
function formatarDataVenda(dataVendaArray) {
    if (!Array.isArray(dataVendaArray) || dataVendaArray.length !== 3) return '';

    const [ano, mes, dia] = dataVendaArray;
    const diaStr = dia.toString().padStart(2, '0');
    const mesStr = mes.toString().padStart(2, '0');
    return `${diaStr}/${mesStr}/${ano}`;
}

// Função para capitalizar texto (primeira letra maiúscula)
function capitalize(text) {
    if (!text) return '';
    return text.charAt(0).toUpperCase() + text.slice(1).toLowerCase();
}


function limparTabela() {
    const tbody = document.querySelector('#historicoTable tbody');
    tbody.innerHTML = '';
}

// Função para mostrar notificação
function showNotification(message, duration = 3000, type = 'info') {
    let notification = document.getElementById('notification');

    if (!notification) {
        notification = document.createElement('div');
        notification.id = 'notification';
        document.body.appendChild(notification);
    }

    notification.className = 'notification'; 
    notification.classList.add(type);
    notification.textContent = message;
    notification.classList.add('show');

    setTimeout(() => {
        notification.classList.remove('show');
    }, duration);
}

// Formata o CPF no campo de input enquanto o usuário digita
function mascaraCpf(input) {
    let value = input.value;
    value = value.replace(/\D/g, ""); // Remove tudo o que não é dígito
    value = value.replace(/(\d{3})(\d)/, "$1.$2"); // Coloca um ponto entre o terceiro e o quarto dígitos
    value = value.replace(/(\d{3})(\d)/, "$1.$2"); // Coloca um ponto entre o terceiro e o quarto dígitos de novo
    value = value.replace(/(\d{3})(\d{1,2})$/, "$1-$2"); // Coloca um hífen entre o terceiro e o quarto dígitos
    input.value = value;
}

