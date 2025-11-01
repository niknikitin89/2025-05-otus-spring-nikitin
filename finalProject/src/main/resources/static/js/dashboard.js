document.addEventListener('DOMContentLoaded', function() {
    // Инициализация всех анимаций
    initAnimations();
debugger;
    function initAnimations() {
        animateCards();
        animateHeader();
        setupButtonEffects();
        setupMenuInteractions();
        animateProgressBar();
    }

    // Анимация появления карточек с задержкой
    function animateCards() {
        const cards = document.querySelectorAll('.card');
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

    // Анимация для header
    function animateHeader() {
        const header = document.querySelector('.header');
        header.style.opacity = '0';
        header.style.transform = 'translateY(-20px)';
        setTimeout(() => {
            header.style.transition = 'all 0.8s ease';
            header.style.opacity = '1';
            header.style.transform = 'translateY(0)';
        }, 300);
    }

    // Эффекты для кнопок с ripple
    function setupButtonEffects() {
        // const buttons = document.querySelectorAll('.btn');
        // buttons.forEach(button => {
        //     button.addEventListener('click', function(e) {
        //         createRippleEffect(this, e);
        //
        //         setTimeout(() => {
        //             alert('Функция "' + this.textContent + '" в разработке!');
        //         }, 300);
        //     });
        // });
    }

    // Создание ripple эффекта
    function createRippleEffect(button, event) {
        const ripple = document.createElement('span');
        const rect = button.getBoundingClientRect();
        const size = Math.max(rect.width, rect.height);
        const x = event.clientX - rect.left - size / 2;
        const y = event.clientY - rect.top - size / 2;

        ripple.style.cssText = `
            position: absolute;
            border-radius: 50%;
            background: rgba(255, 255, 255, 0.6);
            transform: scale(0);
            animation: ripple 0.6s linear;
            width: ${size}px;
            height: ${size}px;
            left: ${x}px;
            top: ${y}px;
        `;

        button.appendChild(ripple);

        setTimeout(() => {
            ripple.remove();
        }, 600);
    }

    // Взаимодействия с меню
    function setupMenuInteractions() {
        const navLinks = document.querySelectorAll('.nav-link');
        navLinks.forEach(link => {
            link.addEventListener('click', function(e) {
                // Проверяем, ведет ли ссылка куда-то
                if (this.getAttribute('href') && this.getAttribute('href') !== '#') {
                    // Это реальная ссылка - разрешаем переход
                    return;
                }

                // Это псевдо-ссылка - блокируем и обрабатываем
                e.preventDefault();
                setActiveMenu(this);
                animateContentTransition();
            });
        });
    }

    // Установка активного пункта меню
    function setActiveMenu(activeLink) {
        const navLinks = document.querySelectorAll('.nav-link');
        navLinks.forEach(l => l.classList.remove('active'));
        activeLink.classList.add('active');
    }

    // Анимация перехода контента
    function animateContentTransition() {
        const mainContent = document.querySelector('.main-content');
        mainContent.style.opacity = '0.7';
        setTimeout(() => {
            mainContent.style.transition = 'opacity 0.3s ease';
            mainContent.style.opacity = '1';
        }, 300);
    }

    // Анимация прогресс-бара
    function animateProgressBar() {
        const progressFill = document.querySelector('.progress-fill');
        setTimeout(() => {
            progressFill.style.width = '37.5%';
        }, 500);
    }

    // Добавляем обработчики для hover эффектов
    function setupHoverEffects() {
        const interactiveElements = document.querySelectorAll('.card, .account-item, .transaction-item');
        interactiveElements.forEach(element => {
            element.addEventListener('mouseenter', function() {
                this.style.transform = this.style.transform || '';
            });

            element.addEventListener('mouseleave', function() {
                this.style.transform = '';
            });
        });
    }

    // Инициализация hover эффектов
    setupHoverEffects();
});