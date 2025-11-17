// ==========================================================
// CONFIGURAÇÃO DA URL DA API
// ==========================================================
const apiUrl = "http://localhost:8080/api/vendedores"; 


let vendedoresPaginados = [];
let paginaAtual = 1;
const vendedoresPorPagina = 10;


let idParaExcluir = null;

// Função para exibir notificações
function showNotification(message, duration = 3000, type = "success") {
    const notification = document.getElementById("notification");
    notification.textContent = message;
    notification.className = "notification " + type; 
    notification.classList.remove("hidden");

    setTimeout(() => {
        notification.classList.add("hidden");
    }, duration);
}

// Função para carregar vendedores do backend e renderizar com paginação
async function carregarVendedores() {
    try {
        const response = await fetch(apiUrl);
        if (!response.ok) {
            const errorText = await response.text();
            console.error('Erro na resposta da API:', response.status, errorText);
            throw new Error(`Erro ao buscar vendedores: ${response.statusText || 'Verifique o console para mais detalhes.'}`);
        }

        const vendedores = await response.json();
        renderVendedores(vendedores);
    } catch (error) {
        console.error("Erro ao carregar vendedores:", error);
        showNotification(error.message, 4000, "error");
    }
}

// Função para renderizar a tabela com vendedores paginados
function renderVendedores(vendedores) {
    vendedoresPaginados = vendedores;
    const tbody = document.querySelector("#vendedoresTable tbody");
    tbody.innerHTML = "";

    const inicio = (paginaAtual - 1) * vendedoresPorPagina;
    const fim = inicio + vendedoresPorPagina;
    const paginaVendedores = vendedores.slice(inicio, fim);

    if (paginaVendedores.length === 0) {
        document.getElementById("emptyState").style.display = "block";
    } else {
        document.getElementById("emptyState").style.display = "none";
    }

    paginaVendedores.forEach((vendedor) => {
        const row = document.createElement("tr");

        row.innerHTML = `
            <td>${vendedor.nome}</td>
            <td>${vendedor.cpf}</td>
            <td>${vendedor.email}</td>
            <td>${vendedor.telefone}</td>
            <td>${vendedor.dataAdmissao ? new Date(vendedor.dataAdmissao).toLocaleDateString('pt-BR') : ""}</td>
            <td>${vendedor.dataDemissao ? new Date(vendedor.dataDemissao).toLocaleDateString('pt-BR') : ""}</td>
            <td>
                <div class="actions">
                    <button class="btn btn-sm btn-warning glow-button" onclick="editarVendedor(${vendedor.id})">✏️ Editar</button>
                    <button class="btn btn-sm btn-danger glow-button" onclick="excluirVendedorModal(${vendedor.id}, '${vendedor.nome}')">🗑️ Excluir</button>
                </div>
            </td>
        `;

        tbody.appendChild(row);
    });
    // Atualiza o contador de vendedores
    document.getElementById("vendedorCount").innerText = `${vendedores.length} vendedores`;

    renderPaginacao();
}

// Função para renderizar botões de paginação
function renderPaginacao() {
    const totalPaginas = Math.ceil(vendedoresPaginados.length / vendedoresPorPagina);
    const wrapper = document.querySelector("#vendedoresTable").parentElement;

    // Remove paginação antiga
    const antiga = document.getElementById("paginacao");
    if (antiga) wrapper.removeChild(antiga);

    if (totalPaginas <= 1) return;

    const paginacao = document.createElement("div");
    paginacao.id = "paginacao";
    paginacao.classList.add("pagination");

    for (let i = 1; i <= totalPaginas; i++) {
        const btn = document.createElement("button");
        btn.textContent = i;
        btn.className = "btn btn-sm " + (i === paginaAtual ? "btn-primary" : "btn-outline");
        btn.addEventListener("click", () => {
            paginaAtual = i;
            renderVendedores(vendedoresPaginados);
        });

        paginacao.appendChild(btn);
    }

    wrapper.appendChild(paginacao);
}

// Abre modal de exclusão com confirmação, estilo cliente.js
function excluirVendedorModal(id, nome) {
    idParaExcluir = id;
    const modal = document.getElementById("modalConfirmacao");
    const message = modal.querySelector("p");
    message.textContent = `Tem certeza que deseja excluir o vendedor "${nome}"?`;
    modal.classList.remove("hidden");

    const yesBtn = document.getElementById("confirmarExclusao");
    const noBtn = document.getElementById("cancelarExclusao");

    // Remove event listeners antigos para não acumular
    const newYesBtn = yesBtn.cloneNode(true);
    const newNoBtn = noBtn.cloneNode(true);
    yesBtn.parentNode.replaceChild(newYesBtn, yesBtn);
    noBtn.parentNode.replaceChild(newNoBtn, noBtn);

    newYesBtn.addEventListener("click", async () => {
        modal.classList.add("hidden");
        if (!idParaExcluir) return;
        try {
            const response = await fetch(`${apiUrl}/${idParaExcluir}`, {
                method: "DELETE",
            });
            if (!response.ok) {
                const errorData = await response.json().catch(() => ({ mensagem: "Erro desconhecido ao excluir." }));
                throw new Error(errorData.mensagem || "Erro ao excluir o vendedor.");
            }
            showNotification(`Vendedor excluído com sucesso!`, 3000, "success");
            carregarVendedores();
        } catch (error) {
            showNotification(error.message, 4000, "error");
        } finally {
            idParaExcluir = null;
        }
    });

    newNoBtn.addEventListener("click", () => {
        modal.classList.add("hidden");
        idParaExcluir = null;
    });
}

// Função para fechar modal caso precise fechar programaticamente
function fecharModalConfirmacao() {
    const modal = document.getElementById("modalConfirmacao");
    modal.classList.add("hidden");
    idParaExcluir = null;
}

// Função para limpar formulário
function limparFormulario() {
    document.getElementById("vendedorForm").reset();
    document.getElementById("id").value = "";
    document.getElementById("senha").value = ""; 
}

// Função para editar vendedor
async function editarVendedor(id) {
    try {
        const response = await fetch(`${apiUrl}/${id}`);
        if (!response.ok) {
            const errorData = await response.json().catch(() => ({ mensagem: "Erro desconhecido ao buscar para edição." }));
            throw new Error(errorData.mensagem || "Erro ao buscar vendedor para edição.");
        }
        const vendedor = await response.json();

        document.getElementById("id").value = vendedor.id;
        document.getElementById("nome").value = vendedor.nome;
        document.getElementById("cpf").value = vendedor.cpf;
        document.getElementById("email").value = vendedor.email;
        document.getElementById("telefone").value = vendedor.telefone;
        document.getElementById("dataAdmissao").value = vendedor.dataAdmissao ? vendedor.dataAdmissao.substring(0, 10) : "";
        document.getElementById("dataDemissao").value = vendedor.dataDemissao ? vendedor.dataDemissao.substring(0, 10) : "";
        document.getElementById("senha").value = ""; 
        showNotification("Vendedor carregado para edição.", 3000, "info");
    } catch (error) {
        showNotification(error.message, 4000, "error");
    }
}

// Inicializar
window.onload = () => {
    carregarVendedores();

    // Associar salvar formulário
    const form = document.getElementById("vendedorForm");
    form.addEventListener("submit", async (event) => {
        event.preventDefault();

        const id = document.getElementById("id").value.trim() || null;
        const method = id ? "PUT" : "POST";
        const url = id ? `${apiUrl}/${id}` : apiUrl;

        const vendedor = {
            id: id ? Number(id) : undefined, 
            nome: document.getElementById("nome").value,
            cpf: document.getElementById("cpf").value,
            email: document.getElementById("email").value,
            telefone: document.getElementById("telefone").value,
            dataAdmissao: document.getElementById("dataAdmissao").value,
            dataDemissao: document.getElementById("dataDemissao").value || null,
            senha: document.getElementById("senha").value,
        };

        
        if (method === "PUT" && vendedor.senha === "") {
            delete vendedor.senha;
        }

        try {
            const response = await fetch(url, {
                method,
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(vendedor),
            });

            if (!response.ok) {
                const errorData = await response.json();
                let errorMessage = "Erro ao salvar o vendedor.";

                if (errorData.erros && Array.isArray(errorData.erros)) {
                    // Erros de validação do Spring
                    errorMessage = errorData.erros.join("<br>"); 
                } else if (errorData.mensagem) {
                    // Erro de regra de negócio (BusinessRuleException)
                    errorMessage = errorData.mensagem;
                }
                throw new Error(errorMessage);
            }

            
            if (method === "POST") {
                const result = await response.json();
                showNotification(result.mensagem || "Vendedor cadastrado com sucesso!", 3000, "success");
            } else { 
                showNotification("Vendedor atualizado com sucesso!", 3000, "success");
            }
            
            limparFormulario();
            carregarVendedores();
        } catch (error) {
            showNotification(error.message, 6000, "error");
        }
    });
};
