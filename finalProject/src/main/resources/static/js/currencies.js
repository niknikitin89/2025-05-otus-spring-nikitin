const API_URL = '/api/v1/currencies';

document.addEventListener('DOMContentLoaded', function () {
    initCurrenciesPage();
});

let currencies = [];
let currentCurrencyId = null;

async function initCurrenciesPage() {
    await loadCurrencies();
    setupEventListeners();
    initAnimations();
}

async function loadCurrencies() {
    try {
        showLoading();
        const response = await fetch(API_URL);
        if (!response.ok) throw new Error('Ошибка загрузки валют');

        currencies = await response.json();
        renderCurrencies();
    } catch (error) {
        showError('Не удалось загрузить список валют');
        console.error('Error loading currencies:', error);
    }
}

function renderCurrencies(filteredCurrencies = null) {
    const currenciesList = document.getElementById('currenciesList');
    const activeOnly = document.getElementById('activeOnly').checked;
    let dataToRender = filteredCurrencies || currencies;

    // Фильтрация по активности
    if (activeOnly) {
        dataToRender = dataToRender.filter(currency => !currency.isDeleted);
    }

    if (dataToRender.length === 0) {
        currenciesList.innerHTML = `
            <div class="empty-state">
                <div class="empty-icon">💰</div>
                <h4>Валюты не найдены</h4>
                <p>Добавьте первую валюту, чтобы начать работу</p>
            </div>
        `;
        return;
    }

    currenciesList.innerHTML = dataToRender.map(currency => `
        <div class="list-item entity-item ${currency.isDeleted ? 'deleted' : ''}" data-currency-id="${currency.id}">
            <div class="item-info">
                <div class="item-title">
                    <span class="currency-symbol">${currency.symbol || '💵'}</span>
                    ${currency.name}
                    <span class="currency-code">(${currency.code})</span>
                    ${currency.isDeleted ? '<span class="currency-status status-deleted">🗑️ Удалена</span>' : '<span class="currency-status status-active">✅ Активна</span>'}
                </div>
                <div class="item-subtitle">
                    <div class="currency-details">
                        <div class="currency-rate">
                            <span class="rate-label">Курс:</span>
                            <span class="rate-value">${formatExchangeRate(currency.exchangeRate)}</span>
                        </div>
                        <div class="currency-dates">
                            <span class="date-label">Создана:</span>
                            <span class="date-value">${formatDate(currency.createdAt)}</span>
                        </div>
                    </div>
                </div>
            </div>
            <div class="item-actions">
                <button class="btn-action edit" onclick="editCurrency(${currency.id})" title="Редактировать">
                    ✏️
                </button>
                ${!currency.isDeleted ? `
                    <button class="btn-action delete" onclick="deleteCurrency(${currency.id})" title="Удалить">
                        🗑️
                    </button>
                ` : `
                    <button class="btn-action restore" onclick="restoreCurrency(${currency.id})" title="Восстановить">
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
        const filteredCurrencies = currencies.filter(currency =>
            currency.name.toLowerCase().includes(searchTerm) ||
            currency.code.toLowerCase().includes(searchTerm)
        );
        renderCurrencies(filteredCurrencies);
    });

    // Чекбокс "Только активные валюты"
    document.getElementById('activeOnly').addEventListener('change', function () {
        renderCurrencies();
    });

    // Форма валюты
    document.getElementById('currencyForm').addEventListener('submit', function (e) {
        e.preventDefault();
        saveCurrency();
    });

    // Автоматическое преобразование кода валюты в верхний регистр
    document.getElementById('currencyCode').addEventListener('input', function (e) {
        e.target.value = e.target.value.toUpperCase();
    });
}

// Модальные окна
function openCurrencyModal(currencyId = null) {
    const modal = document.getElementById('currencyModal');
    const title = document.getElementById('modalTitle');

    if (currencyId) {
        title.textContent = 'Редактировать валюту';
        const currency = currencies.find(c => c.id === currencyId);
        document.getElementById('currencyId').value = currency.id;
        document.getElementById('currencyCode').value = currency.code;
        document.getElementById('currencyName').value = currency.name;
        document.getElementById('currencySymbol').value = currency.symbol || '';
        document.getElementById('exchangeRate').value = currency.exchangeRate || '1.0';
    } else {
        title.textContent = 'Добавить валюту';
        document.getElementById('currencyForm').reset();
        document.getElementById('exchangeRate').value = '1.0';
    }

    modal.style.display = 'block';
}

function closeCurrencyModal() {
    document.getElementById('currencyModal').style.display = 'none';
}

// CRUD операции
async function saveCurrency() {
    const currencyId = document.getElementById('currencyId').value;
    const currencyCode = document.getElementById('currencyCode').value.trim().toUpperCase();
    const currencyName = document.getElementById('currencyName').value.trim();
    const currencySymbol = document.getElementById('currencySymbol').value.trim();
    const exchangeRate = parseFloat(document.getElementById('exchangeRate').value) || 1.0;

    if (!currencyCode || !currencyName) {
        showError('Пожалуйста, заполните обязательные поля');
        return;
    }

    if (currencyCode.length !== 3) {
        showError('Код валюты должен состоять из 3 букв');
        return;
    }

    try {
        const currencyData = {
            code: currencyCode,
            name: currencyName,
            symbol: currencySymbol,
            exchangeRate: exchangeRate
        };
        let response;

        if (currencyId) {
            // Редактирование
            response = await fetch(`${API_URL}/${currencyId}`, {
                method: 'PUT',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(currencyData)
            });
        } else {
            // Создание
            response = await fetch(API_URL, {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(currencyData)
            });
        }

        if (!response.ok) throw new Error('Ошибка сохранения');

        // Очистка currencyId после успешного сохранения
        document.getElementById('currencyId').value = '';

        await loadCurrencies();
        closeCurrencyModal();
        showNotification(currencyId ? 'Валюта успешно обновлена' : 'Валюта успешно создана');

    } catch (error) {
        showError('Ошибка при сохранении валюты');
        console.error('Error saving currency:', error);
    }
}

function editCurrency(currencyId) {
    openCurrencyModal(currencyId);
}

async function deleteCurrency(currencyId) {
    if (!confirm('Вы уверены, что хотите удалить эту валюту?')) {
        return;
    }

    try {
        const currencyData = {isDeleted: true};
        const response = await fetch(`${API_URL}/${currencyId}`, {
            method: 'DELETE',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(currencyData)
        });

        if (!response.ok) throw new Error('Ошибка удаления');

        await loadCurrencies();
        showNotification('Валюта помечена как удаленная');

    } catch (error) {
        showError('Ошибка при удалении валюты');
        console.error('Error deleting currency:', error);
    }
}

async function restoreCurrency(currencyId) {
    try {
        const response = await fetch(`${API_URL}/${currencyId}/restore`, {
            method: 'PUT',
            headers: {'Content-Type': 'application/json'},
        });

        if (!response.ok) throw new Error('Ошибка восстановления');

        await loadCurrencies();
        showNotification('Валюта восстановлена');

    } catch (error) {
        showError('Ошибка при восстановлении валюты');
        console.error('Error restoring currency:', error);
    }
}

// Вспомогательные функции
function formatExchangeRate(rate) {
    if (!rate) return '1.0000';
    return parseFloat(rate).toFixed(4);
}

function formatDate(dateString) {
    if (!dateString) return '—';
    const date = new Date(dateString);
    return date.toLocaleDateString('ru-RU') + ' ' + date.toLocaleTimeString('ru-RU', {
        hour: '2-digit',
        minute: '2-digit'
    });
}

function showLoading() {
    const currenciesList = document.getElementById('currenciesList');
    currenciesList.innerHTML = '<div class="loading">Загрузка валют...</div>';
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
    const modal = document.getElementById('currencyModal');
    if (event.target === modal) {
        closeCurrencyModal();
    }
}