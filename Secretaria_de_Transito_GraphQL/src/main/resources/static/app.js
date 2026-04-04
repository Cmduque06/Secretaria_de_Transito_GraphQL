const endpoint = "/graphql";

const queries = {
    dashboard: `
        query DashboardData {
            propietarios {
                id
                tipo
                identificacion
                nombre
                direccion
                licencia { numero categoria puntosBase puntosActuales }
            }
            vehiculos {
                id
                placa
                marca
                modelo
                tipo
                propietario { id nombre }
            }
            infracciones {
                id
                codigo
                descripcion
                valor
                severidad
                pagada
                vehiculo { id placa }
            }
            vehiculosConMasInfracciones {
                vehiculoId
                placa
                propietarioNombre
                totalInfracciones
            }
            conductoresConMenosPuntos {
                propietarioId
                nombre
                numeroLicencia
                puntosActuales
            }
            totalDineroRecaudadoPorMultas
        }
    `,
    nested: `
        query NestedDetail($id: ID!) {
            propietario(id: $id) {
                id
                nombre
                tipo
                licencia {
                    numero
                    categoria
                    puntosBase
                    puntosActuales
                }
                vehiculos {
                    id
                    placa
                    marca
                    modelo
                    tipo
                    infracciones {
                        id
                        codigo
                        descripcion
                        valor
                        severidad
                        pagada
                    }
                }
            }
        }
    `
};

const elements = {
    metrics: document.querySelector("#metrics"),
    propietariosBody: document.querySelector("#propietarios-body"),
    vehiculosBody: document.querySelector("#vehiculos-body"),
    infraccionesBody: document.querySelector("#infracciones-body"),
    reporteVehiculos: document.querySelector("#reporte-vehiculos"),
    reporteConductores: document.querySelector("#reporte-conductores"),
    totalRecaudado: document.querySelector("#total-recaudado"),
    nestedResult: document.querySelector("#nested-result"),
    detallePropietario: document.querySelector("#detalle-propietario"),
    vehiculoPropietario: document.querySelector("#vehiculo-propietario"),
    infraccionVehiculo: document.querySelector("#infraccion-vehiculo")
};

window.__dashboard = {
    propietarios: [],
    vehiculos: [],
    infracciones: [],
    vehiculosConMasInfracciones: [],
    conductoresConMenosPuntos: [],
    totalDineroRecaudadoPorMultas: 0
};

async function graphQL(query, variables = {}) {
    const response = await fetch(endpoint, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({query, variables})
    });
    const payload = await response.json();
    if (!response.ok || payload.errors?.length) {
        throw new Error(payload.errors?.[0]?.message || "Error consultando GraphQL");
    }
    return payload.data;
}

function formatMoney(value) {
    return new Intl.NumberFormat("es-CO", {style: "currency", currency: "COP", maximumFractionDigits: 0}).format(value);
}

function showToast(message, isError = false) {
    const toast = document.createElement("div");
    toast.className = `toast${isError ? " error" : ""}`;
    toast.textContent = message;
    document.body.appendChild(toast);
    setTimeout(() => toast.remove(), 2600);
}

function fillSelect(select, items, formatter) {
    if (!select) return;
    if (!items.length) {
        select.innerHTML = `<option value="">Sin opciones disponibles</option>`;
        select.disabled = true;
        return;
    }
    select.disabled = false;
    select.innerHTML = items.map((item) => `<option value="${item.id}">${formatter(item)}</option>`).join("");
}

function normalizeDashboard(data) {
    return {
        propietarios: data?.propietarios ?? [],
        vehiculos: data?.vehiculos ?? [],
        infracciones: data?.infracciones ?? [],
        vehiculosConMasInfracciones: data?.vehiculosConMasInfracciones ?? [],
        conductoresConMenosPuntos: data?.conductoresConMenosPuntos ?? [],
        totalDineroRecaudadoPorMultas: data?.totalDineroRecaudadoPorMultas ?? 0
    };
}

function renderMetrics(data) {
    const countPagadas = data.infracciones.filter((item) => item.pagada).length;
    elements.metrics.innerHTML = `
        <article class="metric accent"><span>Propietarios</span><strong>${data.propietarios.length}</strong></article>
        <article class="metric"><span>Vehiculos</span><strong>${data.vehiculos.length}</strong></article>
        <article class="metric"><span>Infracciones</span><strong>${data.infracciones.length}</strong></article>
        <article class="metric"><span>Multas pagadas</span><strong>${countPagadas}</strong></article>
    `;
}

function renderPropietarios(propietarios) {
    elements.propietariosBody.innerHTML = propietarios.map((item) => `
        <tr>
            <td>${item.nombre}<br><small>${item.identificacion}</small></td>
            <td>${item.tipo}</td>
            <td>${item.licencia.numero}</td>
            <td>${item.licencia.puntosActuales}</td>
            <td>
                <div class="mini-actions">
                    <button type="button" data-action="edit-propietario" data-id="${item.id}">Editar</button>
                    <button type="button" class="danger" data-action="delete-propietario" data-id="${item.id}">Eliminar</button>
                </div>
            </td>
        </tr>
    `).join("");
}

function renderVehiculos(vehiculos) {
    elements.vehiculosBody.innerHTML = vehiculos.map((item) => `
        <tr>
            <td>${item.placa}</td>
            <td>${item.marca}</td>
            <td>${item.tipo}</td>
            <td>${item.propietario?.nombre ?? "-"}</td>
            <td>
                <div class="mini-actions">
                    <button type="button" data-action="edit-vehiculo" data-id="${item.id}">Editar</button>
                    <button type="button" class="danger" data-action="delete-vehiculo" data-id="${item.id}">Eliminar</button>
                </div>
            </td>
        </tr>
    `).join("");
}

function renderInfracciones(infracciones) {
    elements.infraccionesBody.innerHTML = infracciones.map((item) => `
        <tr>
            <td>${item.codigo}</td>
            <td>${item.descripcion}</td>
            <td>${item.vehiculo?.placa ?? "-"}</td>
            <td>${formatMoney(item.valor)}</td>
            <td>
                <div class="mini-actions">
                    <button type="button" data-action="edit-infraccion" data-id="${item.id}">Editar</button>
                    <button type="button" class="danger" data-action="delete-infraccion" data-id="${item.id}">Eliminar</button>
                </div>
            </td>
        </tr>
    `).join("");
}

function renderReportes(data) {
    elements.reporteVehiculos.innerHTML = data.vehiculosConMasInfracciones
        .map((item) => `<li>${item.placa} - ${item.propietarioNombre}: ${item.totalInfracciones} infracciones</li>`)
        .join("");
    elements.reporteConductores.innerHTML = data.conductoresConMenosPuntos
        .map((item) => `<li>${item.nombre} - ${item.numeroLicencia}: ${item.puntosActuales} puntos</li>`)
        .join("");
    elements.totalRecaudado.textContent = formatMoney(data.totalDineroRecaudadoPorMultas);
}

async function loadNestedDetail(id) {
    if (!id) {
        elements.nestedResult.textContent = "No hay propietario disponible para mostrar.";
        return;
    }
    const data = await graphQL(queries.nested, {id});
    elements.nestedResult.textContent = JSON.stringify(data.propietario, null, 2);
}

async function loadDashboard() {
    const data = normalizeDashboard(await graphQL(queries.dashboard));
    window.__dashboard = data;
    renderMetrics(data);
    renderPropietarios(data.propietarios);
    renderVehiculos(data.vehiculos);
    renderInfracciones(data.infracciones);
    renderReportes(data);
    fillSelect(elements.vehiculoPropietario, data.propietarios, (item) => `${item.nombre} - ${item.identificacion}`);
    fillSelect(elements.detallePropietario, data.propietarios, (item) => `${item.nombre} (${item.tipo})`);
    fillSelect(elements.infraccionVehiculo, data.vehiculos, (item) => `${item.placa} - ${item.marca} - ${item.propietario?.nombre ?? "Sin propietario"}`);
    if (data.propietarios.length > 0) {
        await loadNestedDetail(data.propietarios[0].id);
    } else {
        elements.nestedResult.textContent = "No hay propietarios registrados.";
    }
}

async function mutate(query, variables, successMessage) {
    await graphQL(query, variables);
    await loadDashboard();
    showToast(successMessage);
}

document.querySelector("#propietario-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    const form = new FormData(event.target);
    await mutate(`
        mutation CreatePropietario($input: PropietarioInput!) {
            crearPropietario(input: $input) { id }
        }
    `, {
        input: {
            tipo: form.get("tipo"),
            identificacion: form.get("identificacion"),
            nombre: form.get("nombre"),
            direccion: form.get("direccion"),
            numeroLicencia: form.get("numeroLicencia"),
            categoriaLicencia: form.get("categoriaLicencia"),
            puntosBase: Number(form.get("puntosBase"))
        }
    }, "Propietario creado");
    event.target.reset();
});

document.querySelector("#vehiculo-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    const form = new FormData(event.target);
    if (!form.get("propietarioId")) {
        showToast("Primero debes crear o tener un propietario disponible.", true);
        return;
    }
    await mutate(`
        mutation CreateVehiculo($input: VehiculoInput!) {
            crearVehiculo(input: $input) { id }
        }
    `, {
        input: {
            placa: form.get("placa"),
            marca: form.get("marca"),
            modelo: form.get("modelo"),
            tipo: form.get("tipo"),
            propietarioId: Number(form.get("propietarioId"))
        }
    }, "Vehiculo creado");
    event.target.reset();
});

document.querySelector("#infraccion-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    const form = new FormData(event.target);
    if (!form.get("vehiculoId")) {
        showToast("Primero debes crear o tener un vehiculo disponible para asignar la infraccion.", true);
        return;
    }
    await mutate(`
        mutation CreateInfraccion($input: InfraccionInput!) {
            crearInfraccion(input: $input) { id }
        }
    `, {
        input: {
            codigo: form.get("codigo"),
            descripcion: form.get("descripcion"),
            valor: Number(form.get("valor")),
            severidad: form.get("severidad"),
            pagada: form.get("pagada") === "true",
            vehiculoId: Number(form.get("vehiculoId"))
        }
    }, "Infraccion creada");
    event.target.reset();
});

document.addEventListener("click", async (event) => {
    const button = event.target.closest("button[data-action]");
    if (!button) return;
    const {action, id} = button.dataset;
    const data = window.__dashboard;
    try {
        if (action === "delete-propietario") {
            await mutate(`mutation($id: ID!) { eliminarPropietario(id: $id) }`, {id: Number(id)}, "Propietario eliminado");
        }
        if (action === "delete-vehiculo") {
            await mutate(`mutation($id: ID!) { eliminarVehiculo(id: $id) }`, {id: Number(id)}, "Vehiculo eliminado");
        }
        if (action === "delete-infraccion") {
            await mutate(`mutation($id: ID!) { eliminarInfraccion(id: $id) }`, {id: Number(id)}, "Infraccion eliminada");
        }
        if (action === "edit-propietario") {
            const item = data.propietarios.find((row) => String(row.id) === id);
            if (!item) throw new Error("No se encontro el propietario a editar");
            const nombre = prompt("Nuevo nombre del propietario", item.nombre);
            if (!nombre) return;
            await mutate(`
                mutation UpdatePropietario($id: ID!, $input: PropietarioInput!) {
                    actualizarPropietario(id: $id, input: $input) { id }
                }
            `, {
                id: Number(id),
                input: {
                    tipo: item.tipo,
                    identificacion: item.identificacion,
                    nombre,
                    direccion: item.direccion ?? "Sin direccion",
                    numeroLicencia: item.licencia.numero,
                    categoriaLicencia: item.licencia.categoria,
                    puntosBase: item.licencia.puntosBase
                }
            }, "Propietario actualizado");
        }
        if (action === "edit-vehiculo") {
            const item = data.vehiculos.find((row) => String(row.id) === id);
            if (!item) throw new Error("No se encontro el vehiculo a editar");
            const marca = prompt("Nueva marca del vehiculo", item.marca);
            if (!marca) return;
            await mutate(`
                mutation UpdateVehiculo($id: ID!, $input: VehiculoInput!) {
                    actualizarVehiculo(id: $id, input: $input) { id }
                }
            `, {
                id: Number(id),
                input: {
                    placa: item.placa,
                    marca,
                    modelo: item.modelo,
                    tipo: item.tipo,
                    propietarioId: Number(item.propietario?.id ?? item.propietarioId)
                }
            }, "Vehiculo actualizado");
        }
        if (action === "edit-infraccion") {
            const item = data.infracciones.find((row) => String(row.id) === id);
            if (!item) throw new Error("No se encontro la infraccion a editar");
            const valor = prompt("Nuevo valor de la infraccion", item.valor);
            if (!valor) return;
            await mutate(`
                mutation UpdateInfraccion($id: ID!, $input: InfraccionInput!) {
                    actualizarInfraccion(id: $id, input: $input) { id }
                }
            `, {
                id: Number(id),
                input: {
                    codigo: item.codigo,
                    descripcion: item.descripcion,
                    valor: Number(valor),
                    severidad: item.severidad,
                    pagada: item.pagada,
                    vehiculoId: Number(item.vehiculo?.id ?? item.vehiculoId)
                }
            }, "Infraccion actualizada");
        }
    } catch (error) {
        showToast(error.message, true);
    }
});

document.querySelectorAll("[data-refresh]").forEach((button) => {
    button.addEventListener("click", async () => {
        try {
            await loadDashboard();
            showToast("Datos actualizados");
        } catch (error) {
            showToast(error.message, true);
        }
    });
});

elements.detallePropietario.addEventListener("change", async (event) => {
    try {
        await loadNestedDetail(Number(event.target.value));
    } catch (error) {
        showToast(error.message, true);
    }
});

document.querySelector("#refresh-reportes").addEventListener("click", async () => {
    try {
        await loadDashboard();
        showToast("Reportes actualizados");
    } catch (error) {
        showToast(error.message, true);
    }
});

loadDashboard().catch((error) => showToast(error.message, true));
