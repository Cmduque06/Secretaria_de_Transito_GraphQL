const endpoint = "/graphql";

const queries = {
    dashboard: `
        query DashboardData {
            propietarios {
                tipo
                identificacion
                nombre
                direccion
                licencia {
                    numero
                    categoria
                    puntosBase
                    puntosActuales
                }
            }
            vehiculos {
                placa
                marca
                modelo
                tipo
                propietarioIdentificacion
                propietario {
                    identificacion
                    nombre
                }
            }
            infracciones {
                id
                codigo
                descripcion
                valor
                severidad
                pagada
                vehiculoPlaca
                vehiculo {
                    placa
                    marca
                    propietario {
                        identificacion
                        nombre
                    }
                }
            }
            vehiculosConMasInfracciones {
                placa
                propietarioIdentificacion
                propietarioNombre
                totalInfracciones
            }
            conductoresConMenosPuntos {
                identificacion
                nombre
                numeroLicencia
                puntosActuales
            }
            totalDineroRecaudadoPorMultas
        }
    `,
    nested: `
        query NestedDetail($identificacion: String!) {
            propietario(identificacion: $identificacion) {
                identificacion
                nombre
                tipo
                direccion
                licencia {
                    numero
                    categoria
                    puntosBase
                    puntosActuales
                }
                vehiculos {
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
    infraccionVehiculo: document.querySelector("#infraccion-vehiculo"),
    propietarioForm: document.querySelector("#propietario-form"),
    vehiculoForm: document.querySelector("#vehiculo-form"),
    infraccionForm: document.querySelector("#infraccion-form"),
    propietarioMode: document.querySelector("#propietario-mode"),
    vehiculoMode: document.querySelector("#vehiculo-mode"),
    infraccionMode: document.querySelector("#infraccion-mode"),
    propietarioSubmit: document.querySelector("#propietario-submit"),
    vehiculoSubmit: document.querySelector("#vehiculo-submit"),
    infraccionSubmit: document.querySelector("#infraccion-submit"),
    cancelarPropietario: document.querySelector("#cancelar-propietario"),
    cancelarVehiculo: document.querySelector("#cancelar-vehiculo"),
    cancelarInfraccion: document.querySelector("#cancelar-infraccion"),
    busquedaPropietarioVehiculo: document.querySelector("#busqueda-propietario-vehiculo"),
    busquedaVehiculos: document.querySelector("#busqueda-vehiculos"),
    busquedaVehiculoInfraccion: document.querySelector("#busqueda-vehiculo-infraccion"),
    busquedaReportes: document.querySelector("#busqueda-reportes")
};

const state = {
    dashboard: emptyDashboard(),
    edit: {
        propietarioIdentificacion: null,
        vehiculoPlaca: null,
        infraccionId: null
    }
};

function emptyDashboard() {
    return {
        propietarios: [],
        vehiculos: [],
        infracciones: [],
        vehiculosConMasInfracciones: [],
        conductoresConMenosPuntos: [],
        totalDineroRecaudadoPorMultas: 0
    };
}

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

function formatMoney(value) {
    return new Intl.NumberFormat("es-CO", {
        style: "currency",
        currency: "COP",
        maximumFractionDigits: 0
    }).format(value);
}

function showToast(message, isError = false) {
    const toast = document.createElement("div");
    toast.className = `toast${isError ? " error" : ""}`;
    toast.textContent = message;
    document.body.appendChild(toast);
    setTimeout(() => toast.remove(), 2600);
}

function includesText(value, term) {
    return String(value ?? "").toLowerCase().includes(String(term ?? "").trim().toLowerCase());
}

function findPropietario(identificacion) {
    return state.dashboard.propietarios.find((item) => item.identificacion === identificacion);
}

function findVehiculo(placa) {
    return state.dashboard.vehiculos.find((item) => item.placa === placa);
}

function fillSelect(select, items, valueKey, formatter) {
    if (!select) return;
    if (!items.length) {
        select.innerHTML = `<option value="">Sin opciones disponibles</option>`;
        select.disabled = true;
        return;
    }
    const previousValue = select.value;
    select.disabled = false;
    select.innerHTML = items
        .map((item) => `<option value="${item[valueKey]}">${formatter(item)}</option>`)
        .join("");
    const hasPreviousValue = items.some((item) => String(item[valueKey]) === String(previousValue));
    select.value = hasPreviousValue ? previousValue : String(items[0][valueKey]);
}

function getFilteredPropietarios() {
    const term = elements.busquedaPropietarioVehiculo.value;
    return state.dashboard.propietarios.filter((item) =>
        includesText(item.nombre, term) || includesText(item.identificacion, term)
    );
}

function getFilteredVehiculosForSelect() {
    const term = elements.busquedaVehiculoInfraccion.value;
    return state.dashboard.vehiculos.filter((item) =>
        includesText(item.placa, term)
        || includesText(item.marca, term)
        || includesText(item.propietario?.nombre, term)
        || includesText(item.propietario?.identificacion, term)
    );
}

function getFilteredVehiculosTable() {
    const term = elements.busquedaVehiculos.value;
    return state.dashboard.vehiculos.filter((item) =>
        includesText(item.placa, term)
        || includesText(item.marca, term)
        || includesText(item.modelo, term)
        || includesText(item.propietario?.nombre, term)
        || includesText(item.propietario?.identificacion, term)
    );
}

function getFilteredVehiculoReportes() {
    const term = elements.busquedaReportes.value;
    return state.dashboard.vehiculosConMasInfracciones.filter((item) =>
        includesText(item.placa, term)
        || includesText(item.propietarioNombre, term)
        || includesText(item.propietarioIdentificacion, term)
    );
}

function getFilteredConductoresReportes() {
    const term = elements.busquedaReportes.value;
    return state.dashboard.conductoresConMenosPuntos.filter((item) =>
        includesText(item.nombre, term)
        || includesText(item.identificacion, term)
        || includesText(item.numeroLicencia, term)
    );
}

function renderMetrics(data) {
    const pagadas = data.infracciones.filter((item) => item.pagada).length;
    elements.metrics.innerHTML = `
        <article class="metric accent"><span>Propietarios</span><strong>${data.propietarios.length}</strong></article>
        <article class="metric"><span>Vehiculos</span><strong>${data.vehiculos.length}</strong></article>
        <article class="metric"><span>Infracciones</span><strong>${data.infracciones.length}</strong></article>
        <article class="metric"><span>Multas pagadas</span><strong>${pagadas}</strong></article>
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
                    <button type="button" data-action="edit-propietario" data-identificacion="${item.identificacion}">Editar</button>
                    <button type="button" class="danger" data-action="delete-propietario" data-identificacion="${item.identificacion}">Eliminar</button>
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
            <td>${item.propietario?.nombre ?? "-"}<br><small>${item.propietario?.identificacion ?? ""}</small></td>
            <td>
                <div class="mini-actions">
                    <button type="button" data-action="edit-vehiculo" data-placa="${item.placa}">Editar</button>
                    <button type="button" class="danger" data-action="delete-vehiculo" data-placa="${item.placa}">Eliminar</button>
                </div>
            </td>
        </tr>
    `).join("");
}

function renderInfracciones(infracciones) {
    elements.infraccionesBody.innerHTML = infracciones.map((item) => `
        <tr>
            <td>${item.codigo}</td>
            <td>${item.descripcion}<br><small>${item.severidad} - ${item.pagada ? "Pagada" : "Pendiente"}</small></td>
            <td>${item.vehiculo?.placa ?? "-"}<br><small>${item.vehiculo?.propietario?.nombre ?? ""}</small></td>
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

function renderReportes() {
    const vehiculos = getFilteredVehiculoReportes();
    const conductores = getFilteredConductoresReportes();

    elements.reporteVehiculos.innerHTML = vehiculos.length
        ? vehiculos.map((item) => `
            <li>${item.placa} - ${item.propietarioNombre} (${item.propietarioIdentificacion}): ${item.totalInfracciones} infracciones</li>
        `).join("")
        : "<li>No hay coincidencias en vehiculos reportados.</li>";

    elements.reporteConductores.innerHTML = conductores.length
        ? conductores.map((item) => `
            <li>${item.nombre} (${item.identificacion}) - ${item.numeroLicencia}: ${item.puntosActuales} puntos</li>
        `).join("")
        : "<li>No hay coincidencias en conductores reportados.</li>";

    elements.totalRecaudado.textContent = formatMoney(state.dashboard.totalDineroRecaudadoPorMultas);
}

function renderSelects() {
    fillSelect(elements.vehiculoPropietario, getFilteredPropietarios(), "identificacion",
        (item) => `${item.nombre} - ${item.identificacion}`);

    fillSelect(elements.infraccionVehiculo, getFilteredVehiculosForSelect(), "placa",
        (item) => `${item.placa} - ${item.marca} - ${item.propietario?.nombre ?? "Sin propietario"}`);

    fillSelect(elements.detallePropietario, state.dashboard.propietarios, "identificacion",
        (item) => `${item.nombre} (${item.identificacion})`);
}

function renderAll() {
    renderMetrics(state.dashboard);
    renderPropietarios(state.dashboard.propietarios);
    renderVehiculos(getFilteredVehiculosTable());
    renderInfracciones(state.dashboard.infracciones);
    renderReportes();
    renderSelects();
}

async function loadNestedDetail(identificacion) {
    if (!identificacion) {
        elements.nestedResult.textContent = "No hay propietario disponible para mostrar.";
        return;
    }
    const data = await graphQL(queries.nested, {identificacion});
    elements.nestedResult.textContent = JSON.stringify(data.propietario, null, 2);
}

async function refreshNestedDetail() {
    const currentValue = elements.detallePropietario.value;
    const selected = state.dashboard.propietarios.some((item) => item.identificacion === currentValue)
        ? currentValue
        : state.dashboard.propietarios[0]?.identificacion;

    if (!selected) {
        elements.nestedResult.textContent = "No hay propietarios registrados.";
        return;
    }

    elements.detallePropietario.value = selected;
    await loadNestedDetail(selected);
}

async function loadDashboard() {
    state.dashboard = normalizeDashboard(await graphQL(queries.dashboard));
    renderAll();
    await refreshNestedDetail();
}

async function mutate(query, variables, successMessage) {
    await graphQL(query, variables);
    await loadDashboard();
    showToast(successMessage);
}

function setPropietarioMode(editing, item = null) {
    const identificacionInput = elements.propietarioForm.elements.identificacion;
    if (!editing) {
        state.edit.propietarioIdentificacion = null;
        elements.propietarioMode.textContent = "Creando propietario nuevo.";
        elements.propietarioSubmit.textContent = "Crear propietario";
        elements.cancelarPropietario.classList.add("hidden");
        elements.propietarioForm.reset();
        identificacionInput.disabled = false;
        elements.propietarioForm.elements.tipo.value = "PERSONA";
        return;
    }

    state.edit.propietarioIdentificacion = item.identificacion;
    elements.propietarioMode.textContent = `Editando propietario ${item.identificacion}.`;
    elements.propietarioSubmit.textContent = "Guardar cambios";
    elements.cancelarPropietario.classList.remove("hidden");
    identificacionInput.disabled = true;
    elements.propietarioForm.elements.tipo.value = item.tipo;
    identificacionInput.value = item.identificacion;
    elements.propietarioForm.elements.nombre.value = item.nombre;
    elements.propietarioForm.elements.direccion.value = item.direccion;
    elements.propietarioForm.elements.numeroLicencia.value = item.licencia.numero;
    elements.propietarioForm.elements.categoriaLicencia.value = item.licencia.categoria;
}

function setVehiculoMode(editing, item = null) {
    const placaInput = elements.vehiculoForm.elements.placa;
    if (!editing) {
        state.edit.vehiculoPlaca = null;
        elements.vehiculoMode.textContent = "Creando vehiculo nuevo.";
        elements.vehiculoSubmit.textContent = "Crear vehiculo";
        elements.cancelarVehiculo.classList.add("hidden");
        elements.vehiculoForm.reset();
        placaInput.disabled = false;
        elements.vehiculoForm.elements.tipo.value = "AUTOMOVIL";
        return;
    }

    state.edit.vehiculoPlaca = item.placa;
    elements.vehiculoMode.textContent = `Editando vehiculo ${item.placa}.`;
    elements.vehiculoSubmit.textContent = "Guardar cambios";
    elements.cancelarVehiculo.classList.remove("hidden");
    placaInput.disabled = true;
    elements.busquedaPropietarioVehiculo.value = item.propietario?.identificacion ?? item.propietarioIdentificacion;
    renderSelects();
    placaInput.value = item.placa;
    elements.vehiculoForm.elements.marca.value = item.marca;
    elements.vehiculoForm.elements.modelo.value = item.modelo;
    elements.vehiculoForm.elements.tipo.value = item.tipo;
    elements.vehiculoForm.elements.propietarioIdentificacion.value = item.propietario?.identificacion ?? item.propietarioIdentificacion;
}

function setInfraccionMode(editing, item = null) {
    if (!editing) {
        state.edit.infraccionId = null;
        elements.infraccionMode.textContent = "Creando infraccion nueva.";
        elements.infraccionSubmit.textContent = "Crear infraccion";
        elements.cancelarInfraccion.classList.add("hidden");
        elements.infraccionForm.reset();
        elements.infraccionForm.elements.severidad.value = "LEVE";
        elements.infraccionForm.elements.pagada.value = "false";
        return;
    }

    state.edit.infraccionId = item.id;
    elements.infraccionMode.textContent = `Editando infraccion #${item.id}.`;
    elements.infraccionSubmit.textContent = "Guardar cambios";
    elements.cancelarInfraccion.classList.remove("hidden");
    elements.busquedaVehiculoInfraccion.value = item.vehiculo?.placa ?? item.vehiculoPlaca;
    renderSelects();
    elements.infraccionForm.elements.codigo.value = item.codigo;
    elements.infraccionForm.elements.descripcion.value = item.descripcion;
    elements.infraccionForm.elements.valor.value = item.valor;
    elements.infraccionForm.elements.severidad.value = item.severidad;
    elements.infraccionForm.elements.pagada.value = String(item.pagada);
    elements.infraccionForm.elements.vehiculoPlaca.value = item.vehiculo?.placa ?? item.vehiculoPlaca;
}

function resetForms() {
    setPropietarioMode(false);
    setVehiculoMode(false);
    setInfraccionMode(false);
}

elements.propietarioForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    const form = new FormData(event.target);
    const input = {
        tipo: form.get("tipo"),
        identificacion: state.edit.propietarioIdentificacion ?? String(form.get("identificacion")).trim(),
        nombre: form.get("nombre"),
        direccion: form.get("direccion"),
        numeroLicencia: form.get("numeroLicencia"),
        categoriaLicencia: form.get("categoriaLicencia")
    };

    try {
        if (state.edit.propietarioIdentificacion) {
            await mutate(`
                mutation UpdatePropietario($identificacion: String!, $input: PropietarioInput!) {
                    actualizarPropietario(identificacion: $identificacion, input: $input) { identificacion }
                }
            `, {
                identificacion: state.edit.propietarioIdentificacion,
                input
            }, "Propietario actualizado");
        } else {
            await mutate(`
                mutation CreatePropietario($input: PropietarioInput!) {
                    crearPropietario(input: $input) { identificacion }
                }
            `, {input}, "Propietario creado");
        }
        setPropietarioMode(false);
    } catch (error) {
        showToast(error.message, true);
    }
});

elements.vehiculoForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    const form = new FormData(event.target);
    const propietarioIdentificacion = form.get("propietarioIdentificacion");

    if (!propietarioIdentificacion) {
        showToast("Selecciona un propietario valido para asignar el vehiculo.", true);
        return;
    }

    const input = {
        placa: (state.edit.vehiculoPlaca ?? String(form.get("placa"))).trim().toUpperCase(),
        marca: form.get("marca"),
        modelo: form.get("modelo"),
        tipo: form.get("tipo"),
        propietarioIdentificacion: String(propietarioIdentificacion).trim()
    };

    try {
        if (state.edit.vehiculoPlaca) {
            await mutate(`
                mutation UpdateVehiculo($placa: String!, $input: VehiculoInput!) {
                    actualizarVehiculo(placa: $placa, input: $input) { placa }
                }
            `, {
                placa: state.edit.vehiculoPlaca,
                input
            }, "Vehiculo actualizado");
        } else {
            await mutate(`
                mutation CreateVehiculo($input: VehiculoInput!) {
                    crearVehiculo(input: $input) { placa }
                }
            `, {input}, "Vehiculo creado");
        }
        setVehiculoMode(false);
    } catch (error) {
        showToast(error.message, true);
    }
});

elements.infraccionForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    const form = new FormData(event.target);
    const vehiculoPlaca = form.get("vehiculoPlaca");

    if (!vehiculoPlaca) {
        showToast("Selecciona un vehiculo valido para asignar la infraccion.", true);
        return;
    }

    const input = {
        codigo: form.get("codigo"),
        descripcion: form.get("descripcion"),
        valor: Number(form.get("valor")),
        severidad: form.get("severidad"),
        pagada: form.get("pagada") === "true",
        vehiculoPlaca: String(vehiculoPlaca).trim().toUpperCase()
    };

    try {
        if (state.edit.infraccionId) {
            await mutate(`
                mutation UpdateInfraccion($id: ID!, $input: InfraccionInput!) {
                    actualizarInfraccion(id: $id, input: $input) { id }
                }
            `, {
                id: Number(state.edit.infraccionId),
                input
            }, "Infraccion actualizada");
        } else {
            await mutate(`
                mutation CreateInfraccion($input: InfraccionInput!) {
                    crearInfraccion(input: $input) { id }
                }
            `, {input}, "Infraccion creada");
        }
        setInfraccionMode(false);
    } catch (error) {
        showToast(error.message, true);
    }
});

document.addEventListener("click", async (event) => {
    const button = event.target.closest("button[data-action]");
    if (!button) return;

    const {action, identificacion, placa, id} = button.dataset;

    try {
        if (action === "delete-propietario") {
            await mutate(`
                mutation DeletePropietario($identificacion: String!) {
                    eliminarPropietario(identificacion: $identificacion)
                }
            `, {identificacion}, "Propietario eliminado");
        }

        if (action === "delete-vehiculo") {
            await mutate(`
                mutation DeleteVehiculo($placa: String!) {
                    eliminarVehiculo(placa: $placa)
                }
            `, {placa}, "Vehiculo eliminado");
        }

        if (action === "delete-infraccion") {
            await mutate(`
                mutation DeleteInfraccion($id: ID!) {
                    eliminarInfraccion(id: $id)
                }
            `, {id: Number(id)}, "Infraccion eliminada");
        }

        if (action === "edit-propietario") {
            const item = findPropietario(identificacion);
            if (!item) throw new Error("No se encontro el propietario a editar");
            setPropietarioMode(true, item);
        }

        if (action === "edit-vehiculo") {
            const item = findVehiculo(placa);
            if (!item) throw new Error("No se encontro el vehiculo a editar");
            setVehiculoMode(true, item);
        }

        if (action === "edit-infraccion") {
            const item = state.dashboard.infracciones.find((row) => String(row.id) === String(id));
            if (!item) throw new Error("No se encontro la infraccion a editar");
            setInfraccionMode(true, item);
        }
    } catch (error) {
        showToast(error.message, true);
    }
});

elements.cancelarPropietario.addEventListener("click", () => setPropietarioMode(false));
elements.cancelarVehiculo.addEventListener("click", () => setVehiculoMode(false));
elements.cancelarInfraccion.addEventListener("click", () => setInfraccionMode(false));

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
        await loadNestedDetail(event.target.value);
    } catch (error) {
        showToast(error.message, true);
    }
});

elements.busquedaPropietarioVehiculo.addEventListener("input", () => {
    renderSelects();
});

elements.busquedaVehiculoInfraccion.addEventListener("input", () => {
    renderSelects();
});

elements.busquedaVehiculos.addEventListener("input", () => {
    renderVehiculos(getFilteredVehiculosTable());
});

elements.busquedaReportes.addEventListener("input", () => {
    renderReportes();
});

document.querySelector("#refresh-reportes").addEventListener("click", async () => {
    try {
        await loadDashboard();
        showToast("Reportes actualizados");
    } catch (error) {
        showToast(error.message, true);
    }
});

resetForms();
loadDashboard().catch((error) => showToast(error.message, true));
