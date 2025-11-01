window.Utils = {
    showNotification: function (message) {
        console.log('Notification:', message);
        alert(message);
    },

    formatDate: function (dateString) {
        if (!dateString) return '—';
        const date = new Date(dateString);
        return date.toLocaleDateString('ru-RU') + ' ' + date.toLocaleTimeString('ru-RU', {
            hour: '2-digit',
            minute: '2-digit'
        });
    },

    showError: function (message) {
        console.error('Error:', message);
        alert('Ошибка: ' + message);
    }
}