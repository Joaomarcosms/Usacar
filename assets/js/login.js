// ==========================================================
// CONFIGURAÇÃO DA API
// ==========================================================
const LOGIN_API_URL = 'http://localhost:8080/api/usuarios/login';

document.addEventListener("DOMContentLoaded", () => {
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', handleLogin);
    }
});

/**
 * Lida com o evento de submissão do formulário de login.
 * @param {Event} event - O evento de submissão.
 */
async function handleLogin(event) {
    event.preventDefault();

    const email = document.getElementById('email').value;
    const senha = document.getElementById('senha').value;

    if (!email || !senha) {
        showNotification("Por favor, preencha o e-mail e a senha.", 3000, "error");
        return;
    }

    const loginDTO = {
        email: email,
        senha: senha
    };

    try {
        const response = await fetch(LOGIN_API_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(loginDTO),
        });

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({ message: 'E-mail ou senha inválidos.' }));
            throw new Error(errorData.message || 'E-mail ou senha inválidos.');
        }

        // Se o login for bem-sucedido, a resposta conterá os dados do usuário
        const usuarioLogado = await response.json();

        // Armazena os dados do usuário logado no localStorage para uso na página index.html
        localStorage.setItem('usuarioLogado', JSON.stringify(usuarioLogado));

        showNotification(`Bem-vindo, ${usuarioLogado.nome}`, 3000, "success");

        // tempo para a notificação ser lida e depois redireciona
        setTimeout(() => {
            window.location.href = 'index.html';
        }, 2000);

    } catch (error) {
        console.error("Falha no login:", error);
        showNotification(error.message, 4000, "error");
    }
}

/**
 * Mostra uma notificação na tela.
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
