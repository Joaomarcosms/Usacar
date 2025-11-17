// ==========================================================
// CONFIGURAÇÃO DA URL DA API
// ==========================================================
const API_URL = "http://localhost:8080/api/carro/estoque";

let todosCarros = [];
let paginaAtual = 1;
const carrosPorPagina = 10;

function filtrarEstoque(event) {
  if (event) event.preventDefault();

  const marca = document.getElementById("marca").value;
  const modelo = document.getElementById("modelo").value;
  const anoFabricacao = document.getElementById("anoFabricacao").value;
  const cor = document.getElementById("cor").value;

  const params = new URLSearchParams();

  if (marca) params.append("marca", marca);
  if (modelo) params.append("modelo", modelo);
  if (anoFabricacao) params.append("anoFabricacao", anoFabricacao);
  if (cor) params.append("cor", cor);

  fetch(`${API_URL}?${params.toString()}`)
    .then(response => {
      if (!response.ok) throw new Error("Erro ao buscar carros");
      return response.json();
    })
    .then(data => {
      todosCarros = data;
      paginaAtual = 1;
      renderizarTabela();
      renderizarPaginacao();
    })
    .catch(error => console.error("Erro:", error));
}

function renderizarTabela() {
  const tbody = document.querySelector("#estoqueTable tbody");
  tbody.innerHTML = "";

  const inicio = (paginaAtual - 1) * carrosPorPagina;
  const fim = inicio + carrosPorPagina;
  const carrosPagina = todosCarros.slice(inicio, fim);

  if (carrosPagina.length === 0) {
    tbody.innerHTML = "<tr><td colspan='7'>Nenhum carro encontrado.</td></tr>";
    return;
  }

  carrosPagina.forEach(carro => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${carro.id || ""}</td>
      <td>${carro.marca}</td>
      <td>${carro.modelo}</td>
      <td>${carro.anoFabricacao}</td>
      <td>${carro.cor}</td>
      <td>${carro.quilometragem}</td>
      <td>${carro.status}</td>
    `;
    tbody.appendChild(tr);
  });
}

function renderizarPaginacao() {
  const paginacaoDiv = document.getElementById("paginacao");
  paginacaoDiv.innerHTML = "";

  const totalPaginas = Math.ceil(todosCarros.length / carrosPorPagina);

  for (let i = 1; i <= totalPaginas; i++) {
    const botao = document.createElement("button");
    botao.textContent = i;
    botao.classList.add("botao-paginacao");
    if (i === paginaAtual) botao.classList.add("ativo");

    botao.addEventListener("click", () => {
      paginaAtual = i;
      renderizarTabela();
      renderizarPaginacao();
    });

    paginacaoDiv.appendChild(botao);
  }
}

document.getElementById("filtroForm").addEventListener("submit", filtrarEstoque);
document.addEventListener("DOMContentLoaded", () => filtrarEstoque(new Event("submit")));
