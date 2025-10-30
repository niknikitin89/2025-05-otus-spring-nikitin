// banks.js - обновленная версия с чекбоксом удаления
const API_URL = '/api/v1/banks';

document.addEventListener('DOMContentLoaded', function() {
    initBanksPage();
});

let banks = [];
let currentBankId = null;

async function initBanksPage() {
    await loadBanks();
    setupEventListeners();
    initAnimations();
}

async function loadBanks() {
    try {
        showLoading();
        breakpoint;
        const response = await fetch(API_URL);
        if (!response.ok) throw new Error('Ошибка загрузки банков');

        banks = await response.json();
        renderBanks();
    } catch (error) {
        showError('Не удалось загрузить список банков');
        console.error('Error loading banks:', error);
    }
}

function renderBanks(filteredBanks = null) {
    const banksList = document.getElementById('banksList');
    const dataToRender = filteredBanks || banks;

    if (dataToRender.length === 0) {
        banksList.innerHTML = `
            <div class="empty-state">
                <div class="empty-icon">🏦</div>
                <h4>Банки не найдены</h4>
                <p>Добавьте первый банк, чтобы начать работу</p>
            </div>
        `;
        return;
    }

    banksList.innerHTML = dataToRender.map(bank => `
        <div class="transaction-item bank-item ${bank.isDeleted ? 'deleted' : ''}" data-bank-id="${bank.id}">
            <div class="transaction-info">
                <div class="transaction-description">
                    ${bank.name}
                    ${bank.isDeleted ? '<span class="bank-status status-deleted">🗑️ Удален</span>' : '<span class="bank-status status-active">✅ Активен</span>'}
                </div>
                <div class="transaction-category">
                    <div class="bank-dates">
                        <div class="date-item">
                            <span class="date-label">Создан:</span>
                            <span class="date-value">${formatDate(bank.createdAt)}</span>
                        </div>
                        ${bank.updatedAt ? `
                        <div class="date-item">
                            <span class="date-label">Обновлен:</span>
                            <span class="date-value">${formatDate(bank.updatedAt)}</span>
                        </div>
                        ` : ''}
                    </div>
                </div>
            </div>
            <div class="bank-actions">
                <button class="btn-action edit" onclick="editBank(${bank.id})" title="Редактировать">
                    ✏️
                </button>
                ${!bank.isDeleted ? `
                    <button class="btn-action delete" onclick="deleteBank(${bank.id})" title="Удалить">
                        🗑️
                    </button>
                ` : `
                    <button class="btn-action restore" onclick="restoreBank(${bank.id})" title="Восстановить">
                        🔄
                    </button>
                `}
            </div>
        </div>
    `).join('');
}

function setupEventListeners() {
    // Поиск
    document.getElementById('searchInput').addEventListener('input', function(e) {
        const searchTerm = e.target.value.toLowerCase();
        const filteredBanks = banks.filter(bank =>
            bank.name.toLowerCase().includes(searchTerm)
        );
        renderBanks(filteredBanks);
    });

    // Форма банка
    document.getElementById('bankForm').addEventListener('submit', function(e) {
        e.preventDefault();
        saveBank();
    });
}

// Модальные окна
function openBankModal(bankId = null) {
    const modal = document.getElementById('bankModal');
    const title = document.getElementById('modalTitle');
    const isDeletedGroup = document.getElementById('isDeletedGroup');

    if (bankId) {
        title.textContent = 'Редактировать банк';
        const bank = banks.find(b => b.id === bankId);
        document.getElementById('bankId').value = bank.id;
        document.getElementById('bankName').value = bank.name;
        document.getElementById('isDeleted').checked = bank.isDeleted;
        isDeletedGroup.style.display = 'block';
    } else {
        title.textContent = 'Добавить банк';
        document.getElementById('bankForm').reset();
        document.getElementById('isDeleted').checked = false;
        isDeletedGroup.style.display = 'none';
    }

    modal.style.display = 'block';
}

function closeBankModal() {
    document.getElementById('bankModal').style.display = 'none';
}

// CRUD операции
async function saveBank() {
    const bankId = document.getElementById('bankId').value;
    const bankName = document.getElementById('bankName').value.trim();
    const isDeleted = document.getElementById('isDeleted').checked;

    if (!bankName) {
        showError('Пожалуйста, введите название банка');
        return;
    }

    try {
        const bankData = {
            name: bankName,
            isDeleted: isDeleted
        };
        let response;

        if (bankId) {
            // Редактирование
            response = await fetch(`${API_URL}/${bankId}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(bankData)
            });
        } else {
            // Создание
            response = await fetch(API_URL, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(bankData)
            });
        }

        if (!response.ok) throw new Error('Ошибка сохранения');

        await loadBanks();
        closeBankModal();
        showNotification(bankId ? 'Банк успешно обновлен' : 'Банк успешно создан');

    } catch (error) {
        showError('Ошибка при сохранении банка');
        console.error('Error saving bank:', error);
    }
}

function editBank(bankId) {
    openBankModal(bankId);
}

async function deleteBank(bankId) {
    try {
        const bankData = { isDeleted: true };
        const response = await fetch(`${API_URL}/${bankId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(bankData)
        });

        if (!response.ok) throw new Error('Ошибка удаления');

        await loadBanks();
        showNotification('Банк помечен как удаленный');

    } catch (error) {
        showError('Ошибка при удалении банка');
        console.error('Error deleting bank:', error);
    }
}

async function restoreBank(bankId) {
    try {
        const bankData = { isDeleted: false };
        const response = await fetch(`${API_URL}/${bankId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(bankData)
        });

        if (!response.ok) throw new Error('Ошибка восстановления');

        await loadBanks();
        showNotification('Банк восстановлен');

    } catch (error) {
        showError('Ошибка при восстановлении банка');
        console.error('Error restoring bank:', error);
    }
}

// Вспомогательные функции
function formatDate(dateString) {
    if (!dateString) return '—';
    const date = new Date(dateString);
    return date.toLocaleDateString('ru-RU') + ' ' + date.toLocaleTimeString('ru-RU', {
        hour: '2-digit',
        minute: '2-digit'
    });
}

function showLoading() {
    const banksList = document.getElementById('banksList');
    banksList.innerHTML = '<div class="loading">Загрузка банков...</div>';
}

function showNotification(message) {
    console.log('Notification:', message);
    alert(message);
}

function showError(message) {
    console.error('Error:', message);
    alert('Ошибка: ' + message);
}

// Анимации
function initAnimations() {
    const cards = document.querySelectorAll('.transactions');
    cards.forEach((card, index) => {
        card.style.opacity = '0';
        card.style.transform = 'translateY(30px) scale(0.95)';
        setTimeout(() => {
            card.style.transition = 'all 0.6s cubic-bezier(0.4, 0, 0.2, 1)';
            card.style.opacity = '1';
            card.style.transform = 'translateY(0) scale(1)';
        }, index * 150);
    });
}

// Закрытие модальных окон при клике вне их
window.onclick = function(event) {
    const modal = document.getElementById('bankModal');
    if (event.target === modal) {
        closeBankModal();
    }
}