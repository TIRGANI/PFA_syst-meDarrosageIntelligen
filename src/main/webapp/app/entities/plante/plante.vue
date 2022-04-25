<template>
  <div>
    <h2 id="page-heading" data-cy="PlanteHeading">
      <span v-text="$t('pfaApp.plante.home.title')" id="plante-heading">Plantes</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('pfaApp.plante.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'PlanteCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-plante"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('pfaApp.plante.home.createLabel')"> Create a new Plante </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && plantes && plantes.length === 0">
      <span v-text="$t('pfaApp.plante.home.notFound')">No plantes found</span>
    </div>
    <div class="table-responsive" v-if="plantes && plantes.length > 0">
      <table class="table table-striped" aria-describedby="plantes">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('pfaApp.plante.lebelle')">Lebelle</span></th>
            <th scope="row"><span v-text="$t('pfaApp.plante.photo')">Photo</span></th>
            <th scope="row"><span v-text="$t('pfaApp.plante.racin')">Racin</span></th>
            <th scope="row"><span v-text="$t('pfaApp.plante.typePlant')">Type Plant</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="plante in plantes" :key="plante.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PlanteView', params: { planteId: plante.id } }">{{ plante.id }}</router-link>
            </td>
            <td>{{ plante.lebelle }}</td>
            <td>{{ plante.photo }}</td>
            <td>{{ plante.racin }}</td>
            <td>
              <div v-if="plante.typePlant">
                <router-link :to="{ name: 'TypePlantView', params: { typePlantId: plante.typePlant.id } }">{{
                  plante.typePlant.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'PlanteView', params: { planteId: plante.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'PlanteEdit', params: { planteId: plante.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(plante)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="$t('entity.action.delete')">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span id="pfaApp.plante.delete.question" data-cy="planteDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-plante-heading" v-text="$t('pfaApp.plante.delete.question', { id: removeId })">
          Are you sure you want to delete this Plante?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-plante"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removePlante()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./plante.component.ts"></script>
