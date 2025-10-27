drop table if exists transactions cascade;
drop table if exists account_balances cascade;
drop table if exists accounts cascade;
drop table if exists currencies cascade;
drop table if exists banks cascade;

-- БАНКИ
create table banks
(
    id         serial primary key,
    name       varchar(255) not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp,
    is_deleted boolean   default false
);

create unique index idx_banks_name_unique on banks (lower(name)) where is_deleted = false;

comment on table banks is 'Таблица для хранения информации о банках';
comment on column banks.id is 'Уникальный идентификатор банка';
comment on column banks.name is 'Название банка';
comment on column banks.created_at is 'Дата и время создания записи';
comment on column banks.updated_at is 'Дата и время последнего обновления записи';
comment on column banks.is_deleted is 'Флаг удаления';

-- ВАЛЮТЫ
create table currencies
(
    id         serial primary key,
    code       varchar(3)   not null, -- RUB, USD
    name       varchar(100) not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp,
    is_deleted boolean   default false
);

create unique index idx_currencies_code_unique on currencies (upper(code)) where is_deleted = false;
create unique index idx_currencies_name_unique on currencies (lower(name)) where is_deleted = false;

comment on table currencies is 'Таблица справочник валют';
comment on column currencies.id is 'Уникальный идентификатор валюты';
comment on column currencies.code is 'Трехбуквенный код валюты по стандарту ISO 4217';
comment on column currencies.name is 'Полное название валюты';
comment on column currencies.created_at is 'Дата и время создания записи';
comment on column currencies.updated_at is 'Дата и время последнего обновления записи';
comment on column currencies.is_deleted is 'Флаг удаления';

-- СЧЕТА
create table accounts
(
    id          serial primary key,
    name        varchar(255) not null,
    bank_id     integer      not null references banks (id),
    currency_id integer      not null references currencies (id),
    created_at  timestamp default current_timestamp,
    updated_at  timestamp default current_timestamp,
    is_deleted  boolean   default false
);

create unique index idx_accounts_name_unique on accounts (lower(name)) where is_deleted = FALSE;

comment on table accounts is 'Таблица финансовых счетов пользователя';
comment on column accounts.id is 'Уникальный идентификатор счета';
comment on column accounts.name is 'Название счета';
comment on column accounts.bank_id is 'Ссылка на банк';
comment on column accounts.currency_id is 'Ссылка на валюту счета';
comment on column accounts.created_at is 'Дата и время создания записи';
comment on column accounts.updated_at is 'Дата и время последнего обновления записи';
comment on column accounts.is_deleted is 'Флаг удаления';

-- ИСТОРИЯ БАЛАНСА СЧЕТОВ
create table account_balances
(
    id           serial primary key,
    account_id   integer        not null references accounts (id),
    balance_date date           not null,
    created_at   timestamp default current_timestamp,
    amount       decimal(15, 2) not null
);

create index idx_account_balances_account_id on account_balances (account_id);
create index idx_account_balances_balance_date on account_balances (balance_date);

comment on table account_balances is 'Таблица для хранения истории балансов счетов';
comment on column account_balances.id is 'Уникальный идентификатор записи';
comment on column account_balances.account_id is 'Ссылка на счет';
comment on column account_balances.balance_date is 'Дата актуальности баланса';
comment on column account_balances.amount is 'Актуальная сумма на счете на указанную дату';
comment on column account_balances.created_at is 'Дата и время создания записи';

-- ФИНАНСОВЫЕ ОПЕРАЦИИ
create table transactions
(
    id               serial primary key,
    account_id       integer        not null references accounts (id),
    amount           decimal(15, 2) not null,
    type             varchar(10)    not null check (type in ('INCOME', 'EXPENSE', 'TRANSFER')),
    description      text,
    transaction_date date           not null,
    created_at       timestamp default current_timestamp,
    updated_at       timestamp default current_timestamp,
    is_deleted       boolean   default false
);

create index idx_transactions_account_id on transactions (account_id);
create index idx_transactions_date on transactions (transaction_date);
create index idx_transactions_type on transactions (type);

comment on table transactions is 'Таблица финансовых операций';
comment on column transactions.id is 'Уникальный идентификатор операции';
comment on column transactions.account_id is 'Счет, к которому относится операция';
comment on column transactions.amount is 'Сумма операции';
comment on column transactions.type is 'Тип операции';
comment on column transactions.description is 'Описание операции';
comment on column transactions.transaction_date is 'Дата совершения операции';
comment on column transactions.created_at is 'Дата и время создания записи';
comment on column transactions.updated_at is 'Дата и время последнего обновления записи';
comment on column transactions.is_deleted is 'Флаг удаления';