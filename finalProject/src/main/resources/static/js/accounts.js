const API_URL = '/api/v1/accounts';
const BANKS_API_URL = '/api/v1/banks';
const CURRENCIES_API_URL = '/api/v1/currencies';

document.addEventListener('DOMContentLoaded', function () {
    initAccountsPage();
});

let accounts = [];
let banks = [];
let currencies = [];
let currentAccountId = null;

async function initAccountsPage() {
    await loadBanksAndCurrencies();
    await loadAccounts();
    setupEventListeners();
    initAnimations();
}

async function loadBanksAndCurrencies() {
    try {
        // Загружаем банки и валюты для выпадающих списков
        const [banksResponse, currenciesResponse] = await Promise.all([
            fetch(BANKS_API_URL),
            fetch(CURRENCIES_API_URL)
        ]);

        if (!banksResponse.ok || !currenciesResponse.ok) {
            throw new Error('Ошибка загрузки данных');
        }

        banks = await banksResponse.json();
        currencies = await currenciesResponse.json();

        // Заполняем выпадающие списки
        populateBanksDropdown();
        populateCurrenciesDropdown();

    } catch (error) {
        window.Utils.showError('Не удалось загрузить данные банков и валют');
        console.error('Error loading banks and currencies:', error);
    }
}

function populateBanksDropdown() {
    const bankSelect = document.getElementById('accountBank');
    bankSelect.innerHTML = '<option value="">Выберите банк</option>';

    banks.filter(bank => !bank.isDeleted).forEach(bank => {
        const option = document.createElement('option');
        option.value = bank.id;
        option.textContent = bank.name;
        bankSelect.appendChild(option);
    });
}

function populateCurrenciesDropdown() {
    const currencySelect = document.getElementById('accountCurrency');
    currencySelect.innerHTML = '<option value="">Выберите валюту</option>';

    currencies.filter(currency => !currency.isDeleted).forEach(currency => {
        const option = document.createElement('option');
        option.value = currency.id;
        option.textContent = `${currency.name} (${currency.code})`;
        currencySelect.appendChild(option);
    });
}

async function loadAccounts() {
    try {
        showLoading();
        const response = await fetch(API_URL);
        if (!response.ok) throw new Error('Ошибка загрузки счетов');

        accounts = await response.json();
        renderAccounts();
    } catch (error) {
        window.Utils.showError('Не удалось загрузить список счетов');
        console.error('Error loading accounts:', error);
    }
}

function renderAccounts(filteredAccounts = null) {
    const accountsList = document.getElementById('accountsList');
    const activeOnly = document.getElementById('activeOnly').checked;
    let dataToRender = filteredAccounts || accounts;

    // Фильтрация по активности
    if (activeOnly) {
        dataToRender = dataToRender.filter(account => !account.isDeleted);
    }

    if (dataToRender.length === 0) {
        accountsList.innerHTML = `
            <div class="empty-state">
                <div class="empty-icon">💳</div>
                <h4>Счета не найдены</h4>
                <p>Добавьте первый счет, чтобы начать работу</p>
            </div>
        `;
        return;
    }

    accountsList.innerHTML = dataToRender.map(account => `
        <div class="list-item entity-item ${account.isDeleted ? 'deleted' : ''}" data-account-id="${account.id}">
            <div class="item-info">
                <div class="item-title">
                    <span class="account-symbol">💳</span>
                    ${account.name}
                    ${account.isDeleted ?
        '<span class="item-status status-deleted">🗑️ Удален</span>'
        : '<span class="item-status status-active">✅ Активен</span>'}
                </div>
                <div class="item-subtitle">
                    <div class="account-details">
                        <div class="account-info-item">
                            <span class="account-info-label">Банк:</span>
                            <span class="account-bank">${account.bank?.name || 'Не указан'}</span>
                        </div>
                        <div class="account-info-item">
                            <span class="account-info-label">Валюта:</span>
                            <span class="account-currency">${account.currency?.name || 'Не указана'} (${account.currency?.code || '—'})</span>
                        </div>
                        <div class="item-dates">
                            <span class="date-label">Создан:</span>
                            <span class="date-value">${window.Utils.formatDate(account.createdAt)}</span>
                        </div>
                        ${account.updatedAt ? `
                        <div class="item-dates">
                            <span class="date-label">Обновлен:</span>
                            <span class="date-value">${window.Utils.formatDate(account.updatedAt)}</span>
                        </div>
                        ` : ''}
                    </div>
                </div>
            </div>
            <div class="item-actions">
                <button class="btn-action edit" onclick="editAccount(${account.id})" title="Редактировать">
                    ✏️
                </button>
                ${!account.isDeleted ? `
                    <button class="btn-action delete" onclick="deleteAccount(${account.id})" title="Удалить">
                        🗑️
                    </button>
                ` : `
                    <button class="btn-action restore" onclick="restoreAccount(${account.id})" title="Восстановить">
                        🔄
                    </button>
                `}
            </div>
        </div>
    `).join('');
}

function setupEventListeners() {
    // Поиск
    document.getElementById('searchInput').addEventListener('input', function (e) {
        const searchTerm = e.target.value.toLowerCase();
        const filteredAccounts = accounts.filter(account =>
            account.name.toLowerCase().includes(searchTerm) ||
            account.bank?.name.toLowerCase().includes(searchTerm) ||
            account.currency?.name.toLowerCase().includes(searchTerm)
        );
        renderAccounts(filteredAccounts);
    });

    // Чекбокс "Только активные счета"
    document.getElementById('activeOnly').addEventListener('change', function () {
        renderAccounts();
    });

    // Форма счета
    document.getElementById('accountForm')
        .addEventListener('submit', function (e) {
            e.preventDefault();
            saveAccount();
        });
}

// Модальные окна
function openAccountModal(accountId = null) {
    const modal = document.getElementById('accountModal');
    const title = document.getElementById('modalTitle');

    if (accountId) {
        title.textContent = 'Редактировать счет';
        const account = accounts.find(a => a.id === accountId);
        document.getElementById('accountId').value = account.id;
        document.getElementById('accountName').value = account.name;
        document.getElementById('accountBank').value = account.bank?.id || '';
        document.getElementById('accountCurrency').value = account.currency?.id || '';
    } else {
        title.textContent = 'Добавить счет';
        document.getElementById('accountForm').reset();
    }

    modal.style.display = 'block';
}

function closeAccountModal() {
    document.getElementById('accountModal').style.display = 'none';
}

// CRUD операции
async function saveAccount() {
    const accountId = document.getElementById('accountId').value;
    const accountName = document.getElementById('accountName').value.trim();
    const bankId = document.getElementById('accountBank').value;
    const currencyId = document.getElementById('accountCurrency').value;

    if (!accountName || !bankId || !currencyId) {
        window.Utils.showError('Пожалуйста, заполните все обязательные поля');
        return;
    }

    try {
        const accountData = {
            name: accountName,
            bank: {id: parseInt(bankId)},
            currency: {id: parseInt(currencyId)}
        };
        let response;

        if (accountId) {
            // Редактирование
            response = await fetch(`${API_URL}/${accountId}`, {
                method: 'PUT',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(accountData)
            });
        } else {
            // Создание
            response = await fetch(API_URL, {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(accountData)
            });
        }

        if (!response.ok) throw new Error('Ошибка сохранения');

        // Очистка accountId после успешного сохранения
        document.getElementById('accountId').value = '';

        await loadAccounts();
        closeAccountModal();

    } catch (error) {
        window.Utils.showError('Ошибка при сохранении счета');
        console.error('Error saving account:', error);
    }
}

function editAccount(accountId) {
    openAccountModal(accountId);
}

async function deleteAccount(accountId) {
    if (!confirm('Вы уверены, что хотите удалить этот счет?')) {
        return;
    }

    try {
        const accountData = {isDeleted: true};
        const response = await fetch(`${API_URL}/${accountId}`, {
            method: 'DELETE',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(accountData)
        });

        if (!response.ok) throw new Error('Ошибка удаления');

        await loadAccounts();

    } catch (error) {
        window.Utils.showError('Ошибка при удалении счета');
        console.error('Error deleting account:', error);
    }
}

async function restoreAccount(accountId) {
    try {
        const response = await fetch(`${API_URL}/${accountId}/restore`, {
            method: 'PUT',
            headers: {'Content-Type': 'application/json'},
        });

        if (!response.ok) throw new Error('Ошибка восстановления');

        await loadAccounts();

    } catch (error) {
        window.Utils.showError('Ошибка при восстановлении счета');
        console.error('Error restoring account:', error);
    }
}

// Вспомогательные функции
function showLoading() {
    const accountsList = document.getElementById('accountsList');
    accountsList.innerHTML = '<div class="loading">Загрузка счетов...</div>';
}

// Анимации
function initAnimations() {
    const cards = document.querySelectorAll('.content-card');
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
window.onclick = function (event) {
    const modal = document.getElementById('accountModal');
    if (event.target === modal) {
        closeAccountModal();
    }
}