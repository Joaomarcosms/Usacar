// ==========================================================
// CONFIGURAÇÃO DA API
// ==========================================================
const USUARIOS_API_URL = 'http://localhost:8080/api/usuarios';

document.addEventListener("DOMContentLoaded", () => {
    const cadastroForm = document.getElementById('cadastroForm');
    if (cadastroForm) {
        cadastroForm.addEventListener('submit', handleCadastro);
    }
});

/**
 * Lida com o evento de submissão do formulário de cadastro.
 * @param {Event} event - O evento de submissão.
 */
async function handleCadastro(event) {
    event.preventDefault(); // Impede o recarregamento da página

    // Captura os valores dos campos do formulário
    const nome = document.getElementById('nome').value;
    const cpf = document.getElementById('cpf').value;
    const email = document.getElementById('email').value;
    const senha = document.getElementById('senha').value;
    const perfil = document.getElementById('perfil').value;

    // Validação simples para garantir que todos os campos foram preenchidos
    if (!nome || !cpf || !email || !senha || !perfil) {
        showNotification("Por favor, preencha todos os campos.", 3000, "error");
        return;
    }

    // Monta o objeto DTO para enviar na requisição
    const usuarioDTO = {
        nome: nome,
        cpf: cpf,
        email: email,
        senha: senha,
        perfil: perfil
    };

    try {
        // Envia os dados para o backend usando o método POST
        const response = await fetch(USUARIOS_API_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(usuarioDTO),
        });

        // Se a resposta não for bem-sucedida, lê a mensagem de erro do backend
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || `Erro ${response.status}: Não foi possível cadastrar o usuário.`);
        }

        // Se a resposta for bem-sucedida
        const usuarioCriado = await response.json();
        console.log('Usuário criado:', usuarioCriado);
        showNotification(`Usuário "${usuarioCriado.nome}" cadastrado com sucesso!`, 4000, "success");

        // Limpa o formulário após o sucesso
        document.getElementById('cadastroForm').reset();

    } catch (error) {
        console.error("Falha no cadastro:", error);
        showNotification(error.message, 5000, "error");
    }
}

/**
 * Mostra uma notificação na tela.
 * @param {string} message - A mensagem a ser exibida.
 * @param {number} duration - A duração em milissegundos.
 * @param {string} type - O tipo da notificação ('success' ou 'error').
 */
function showNotification(message, duration = 3000, type = "success") {
    const notification = document.getElementById("notification");
    if (!notification) return;
    notification.textContent = message;
    notification.className = `notification show ${type}`;
    setTimeout(() => {
        notification.className = 'notification hidden';
    }, duration);
}
