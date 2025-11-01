const API_URL = '/api/v1/transactions';
const ACCOUNTS_API_URL = '/api/v1/accounts';

document.addEventListener('DOMContentLoaded', function () {
    initTransactionsPage();
});

let transactions = [];
let accounts = [];
let currentTransactionId = null;
let currentFilters = {
    accounts: [],
    dateFrom: '',
    dateTo: '',
    type: '',
    search: ''
};

let choicesInstance;

async function initTransactionsPage() {
    initChoices(); // Инициализируем Choices.js
    await loadAccounts();
    await loadTransactions();
    setupEventListeners();
    initAnimations();
    setupFilters();
}

function initChoices() {
    const element = document.getElementById('accountFilter');
    choicesInstance = new Choices(element, {
        removeItemButton: true,
        searchEnabled: true,
        searchPlaceholderValue: 'Поиск счетов...',
        placeholder: true,
        placeholderValue: 'Выберите счета',
        noResultsText: 'Счета не найдены',
        noChoicesText: 'Все счета выбраны',
        itemSelectText: 'Нажмите для выбора',
        shouldSort: false,
        position: 'bottom'
    });
}

async function loadAccounts() {
    try {
        const response = await fetch(ACCOUNTS_API_URL);
        if (!response.ok) throw new Error('Ошибка загрузки счетов');

        accounts = await response.json();
        populateAccountsDropdown();
        populateAccountsFilter();

    } catch (error) {
        window.Utils.showError('Не удалось загрузить список счетов');
        console.error('Error loading accounts:', error);
    }
}

function populateAccountsDropdown() {
    const accountSelect = document.getElementById('transactionAccount');
    accountSelect.innerHTML = '<option value="">Выберите счет</option>';

    accounts.filter(account => !account.isDeleted).forEach(account => {
        const option = document.createElement('option');
        option.value = account.id;
        option.textContent = `${account.name} (${account.bank?.name}) - ${account.currency?.code}`;
        accountSelect.appendChild(option);
    });
}

function populateAccountsFilter() {
    // Очищаем текущие опции
    if (choicesInstance) {
        choicesInstance.removeActiveItems();
        choicesInstance.clearStore();
    }

    // Добавляем опцию "Все счета"
    choicesInstance.setChoices([
        { value: '', label: 'Все счета', selected: true }
    ], 'value', 'label', false);

    // Добавляем активные счета
    const accountOptions = accounts
        .filter(account => !account.isDeleted)
        .map(account => ({
            value: account.id,
            label: `💳 ${account.name} (${account.bank?.name} - ${account.currency?.code})`
        }));

    choicesInstance.setChoices(accountOptions, 'value', 'label', true);

    // const accountFilter = document.getElementById('accountFilter');
    // accountFilter.innerHTML = '<option value="">Все счета</option>';
    //
    // accounts.filter(account => !account.isDeleted).forEach(account => {
    //     const option = document.createElement('option');
    //     option.value = account.id;
    //     option.textContent = `${account.name} (${account.bank?.name})`;
    //     accountFilter.appendChild(option);
    // });
}

function setupFilters() {
    // Устанавливаем текущую дату как дату "по умолчанию" для поля даты
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('transactionDate').value = today;
}

async function loadTransactions() {
    try {
        showLoading();
debugger;
        // Строим URL с параметрами фильтрации
        const url = new URL(API_URL, window.location.origin);

        if (currentFilters.accounts.length > 0) {
            currentFilters.accounts.forEach(accountId => {
                url.searchParams.append('accountIds', accountId);
            });
        }

        if (currentFilters.dateFrom) {
            url.searchParams.append('dateFrom', currentFilters.dateFrom);
        }

        if (currentFilters.dateTo) {
            url.searchParams.append('dateTo', currentFilters.dateTo);
        }

        if (currentFilters.type) {
            url.searchParams.append('type', currentFilters.type);
        }

        const response = await fetch(url);
        if (!response.ok) throw new Error('Ошибка загрузки транзакций');

        transactions = await response.json();
        renderTransactions();
    } catch (error) {
        window.Utils.showError('Не удалось загрузить список транзакций');
        console.error('Error loading transactions:', error);
    }
}

function renderTransactions(filteredTransactions = null) {
    const transactionsList = document.getElementById('transactionsList');
    const activeOnly = document.getElementById('activeOnly').checked;
    let dataToRender = filteredTransactions || transactions;

    // Фильтрация по активности
    if (activeOnly) {
        dataToRender = dataToRender.filter(transaction => !transaction.isDeleted);
    }

    // Дополнительная фильтрация по поиску (на клиенте)
    if (currentFilters.search) {
        const searchTerm = currentFilters.search.toLowerCase();
        dataToRender = dataToRender.filter(transaction =>
            transaction.description?.toLowerCase().includes(searchTerm)
        );
    }

    if (dataToRender.length === 0) {
        transactionsList.innerHTML = `
            <div class="empty-state">
                <div class="empty-icon">🔄</div>
                <h4>Транзакции не найдены</h4>
                <p>Добавьте первую транзакцию или измените параметры фильтрации</p>
            </div>
        `;
        return;
    }

    transactionsList.innerHTML = dataToRender.map(transaction => {
        const amountClass = getAmountClass(transaction.type);
        const typeClass = getTypeClass(transaction.type);
        const typeText = getTypeText(transaction.type);
        const amountSign = transaction.type === 'EXPENSE' ? '-' : '+';

        return `
        <div class="list-item entity-item ${transaction.isDeleted ? 'deleted' : ''}" data-transaction-id="${transaction.id}">
            <div class="item-info">
                <div class="item-title">
                    ${transaction.description || 'Без описания'}
                    <span class="transaction-type ${typeClass}">${typeText}</span>
                    ${transaction.isDeleted ?
            '<span class="item-status status-deleted">🗑️ Удалена</span>'
            : '<span class="item-status status-active">✅ Активна</span>'}
                </div>
                <div class="item-subtitle">
                    <div class="transaction-details">
                        <div class="transaction-info-item">
                            <span class="transaction-info-label">Счет:</span>
                            <span class="account-bank">${transaction.account?.name || 'Не указан'} (${transaction.account?.bank?.name || '—'})</span>
                        </div>
                        <div class="transaction-info-item">
                            <span class="transaction-info-label">Дата:</span>
                            <span class="date-value">${formatTransactionDate(transaction.transactionDate)}</span>
                        </div>
                        <div class="item-dates">
                            <span class="date-label">Создана:</span>
                            <span class="date-value">${window.Utils.formatDate(transaction.createdAt)}</span>
                        </div>
                        ${transaction.updatedAt ? `
                        <div class="item-dates">
                            <span class="date-label">Обновлена:</span>
                            <span class="date-value">${window.Utils.formatDate(transaction.updatedAt)}</span>
                        </div>
                        ` : ''}
                    </div>
                </div>
            </div>
            <div class="transaction-amount ${amountClass}">
                ${amountSign}${formatAmount(transaction.amount)} ${transaction.account?.currency?.code || ''}
            </div>
            <div class="item-actions">
                <button class="btn-action edit" onclick="editTransaction(${transaction.id})" title="Редактировать">
                    ✏️
                </button>
                ${!transaction.isDeleted ? `
                    <button class="btn-action delete" onclick="deleteTransaction(${transaction.id})" title="Удалить">
                        🗑️
                    </button>
                ` : `
                    <button class="btn-action restore" onclick="restoreTransaction(${transaction.id})" title="Восстановить">
                        🔄
                    </button>
                `}
            </div>
        </div>
        `;
    }).join('');
}

function setupEventListeners() {
    // Поиск
    document.getElementById('searchInput').addEventListener('input', function (e) {
        currentFilters.search = e.target.value.toLowerCase();
        renderTransactions();
    });

    // Чекбокс "Только активные транзакции"
    document.getElementById('activeOnly').addEventListener('change', function () {
        renderTransactions();
    });

    // Форма транзакции
    document.getElementById('transactionForm').addEventListener('submit', function (e) {
        e.preventDefault();
        saveTransaction();
    });

    // Закрытие Choices при клике вне области
    document.addEventListener('click', function(event) {
        const choices = document.querySelector('.choices');
        if (choices && !choices.contains(event.target)) {
            const dropdown = choices.querySelector('.choices__list--dropdown');
            if (dropdown && dropdown.classList.contains('is-active')) {
                choicesInstance.hideDropdown();
            }
        }
    });
}

// Функции фильтрации
function applyFilters() {
    // Получаем выбранные значения из Choices
    const selectedValues = choicesInstance.getValue(true);
    const selectedAccounts = selectedValues
        .map(item => item.value)
        .filter(value => value !== ''); // Исключаем "Все счета"

    const accountFilter = document.getElementById('accountFilter');
    const dateFrom = document.getElementById('dateFrom').value;
    const dateTo = document.getElementById('dateTo').value;
    const typeFilter = document.getElementById('typeFilter').value;

    currentFilters.accounts = selectedAccounts;// Array.from(accountFilter.selectedOptions).map(option => option.value).filter(Boolean);
    currentFilters.dateFrom = dateFrom;
    currentFilters.dateTo = dateTo;
    currentFilters.type = typeFilter;

    loadTransactions(); // Перезагружаем с сервера с новыми фильтрами
}

function resetFilters() {
    // Сбрасываем Choices
    if (choicesInstance) {
        choicesInstance.removeActiveItems();
        choicesInstance.setChoiceByValue('');
    }

    document.getElementById('accountFilter').selectedIndex = -1;
    document.getElementById('dateFrom').value = '';
    document.getElementById('dateTo').value = '';
    document.getElementById('typeFilter').value = '';
    document.getElementById('searchInput').value = '';

    currentFilters = {
        accounts: [],
        dateFrom: '',
        dateTo: '',
        type: '',
        search: ''
    };

    loadTransactions();
}

// Модальные окна
function openTransactionModal(transactionId = null) {
    const modal = document.getElementById('transactionModal');
    const title = document.getElementById('modalTitle');

    if (transactionId) {
        title.textContent = 'Редактировать транзакцию';
        const transaction = transactions.find(t => t.id === transactionId);
        document.getElementById('transactionId').value = transaction.id;
        document.getElementById('transactionAccount').value = transaction.account?.id || '';
        document.getElementById('transactionType').value = transaction.type;
        document.getElementById('transactionAmount').value = transaction.amount;
        document.getElementById('transactionDate').value = transaction.transactionDate;
        document.getElementById('transactionDescription').value = transaction.description || '';
    } else {
        title.textContent = 'Добавить транзакцию';
        document.getElementById('transactionForm').reset();
        // Устанавливаем текущую дату по умолчанию
        const today = new Date().toISOString().split('T')[0];
        document.getElementById('transactionDate').value = today;
    }

    modal.style.display = 'block';
}

function closeTransactionModal() {
    document.getElementById('transactionModal').style.display = 'none';
}

// CRUD операции
async function saveTransaction() {
    const transactionId = document.getElementById('transactionId').value;
    const accountId = document.getElementById('transactionAccount').value;
    const type = document.getElementById('transactionType').value;
    const amount = parseFloat(document.getElementById('transactionAmount').value);
    const transactionDate = document.getElementById('transactionDate').value;
    const description = document.getElementById('transactionDescription').value.trim();

    if (!accountId || !type || !amount || !transactionDate) {
        window.Utils.showError('Пожалуйста, заполните все обязательные поля');
        return;
    }

    if (amount <= 0) {
        window.Utils.showError('Сумма должна быть больше 0');
        return;
    }

    try {
        const transactionData = {
            accountId: parseInt(accountId),
            type: type,
            amount: amount,
            transactionDate: transactionDate,
            description: description
        };
        let response;

        if (transactionId) {
            // Редактирование
            response = await fetch(`${API_URL}/${transactionId}`, {
                method: 'PUT',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(transactionData)
            });
        } else {
            // Создание
            response = await fetch(API_URL, {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(transactionData)
            });
        }

        if (!response.ok) throw new Error('Ошибка сохранения');

        // Очистка transactionId после успешного сохранения
        document.getElementById('transactionId').value = '';

        await loadTransactions();
        closeTransactionModal();

    } catch (error) {
        window.Utils.showError('Ошибка при сохранении транзакции');
        console.error('Error saving transaction:', error);
    }
}

function editTransaction(transactionId) {
    openTransactionModal(transactionId);
}

async function deleteTransaction(transactionId) {
    if (!confirm('Вы уверены, что хотите удалить эту транзакцию?')) {
        return;
    }

    try {
        const transactionData = {isDeleted: true};
        const response = await fetch(`${API_URL}/${transactionId}`, {
            method: 'DELETE',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(transactionData)
        });

        if (!response.ok) throw new Error('Ошибка удаления');

        await loadTransactions();

    } catch (error) {
        window.Utils.showError('Ошибка при удалении транзакции');
        console.error('Error deleting transaction:', error);
    }
}

async function restoreTransaction(transactionId) {
    try {
        const response = await fetch(`${API_URL}/${transactionId}/restore`, {
            method: 'PUT',
            headers: {'Content-Type': 'application/json'},
        });

        if (!response.ok) throw new Error('Ошибка восстановления');

        await loadTransactions();

    } catch (error) {
        window.Utils.showError('Ошибка при восстановлении транзакции');
        console.error('Error restoring transaction:', error);
    }
}

// Вспомогательные функции
function getAmountClass(type) {
    switch (type) {
        case 'INCOME': return 'income';
        case 'EXPENSE': return 'expense';
        case 'TRANSFER': return 'transfer';
        default: return '';
    }
}

function getTypeClass(type) {
    switch (type) {
        case 'INCOME': return 'type-income';
        case 'EXPENSE': return 'type-expense';
        case 'TRANSFER': return 'type-transfer';
        default: return '';
    }
}

function getTypeText(type) {
    switch (type) {
        case 'INCOME': return 'Доход';
        case 'EXPENSE': return 'Расход';
        case 'TRANSFER': return 'Перевод';
        default: return type;
    }
}

function formatAmount(amount) {
    return parseFloat(amount).toFixed(2);
}

function formatTransactionDate(dateString) {
    if (!dateString) return '—';
    const date = new Date(dateString);
    return date.toLocaleDateString('ru-RU');
}

function showLoading() {
    const transactionsList = document.getElementById('transactionsList');
    transactionsList.innerHTML = '<div class="loading">Загрузка транзакций...</div>';
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
    const modal = document.getElementById('transactionModal');
    if (event.target === modal) {
        closeTransactionModal();
    }
}