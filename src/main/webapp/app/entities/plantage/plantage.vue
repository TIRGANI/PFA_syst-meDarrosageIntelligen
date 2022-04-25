<template>
  <div>
    <h2 id="page-heading" data-cy="PlantageHeading">
      <span v-text="$t('pfaApp.plantage.home.title')" id="plantage-heading">Plantages</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('pfaApp.plantage.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'PlantageCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-plantage"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('pfaApp.plantage.home.createLabel')"> Create a new Plantage </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && plantages && plantages.length === 0">
      <span v-text="$t('pfaApp.plantage.home.notFound')">No plantages found</span>
    </div>
    <div class="table-responsive" v-if="plantages && plantages.length > 0">
      <table class="table table-striped" aria-describedby="plantages">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('pfaApp.plantage.date')">Date</span></th>
            <th scope="row"><span v-text="$t('pfaApp.plantage.nbrPlate')">Nbr Plate</span></th>
            <th scope="row"><span v-text="$t('pfaApp.plantage.plante')">Plante</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="plantage in plantages" :key="plantage.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PlantageView', params: { plantageId: plantage.id } }">{{ plantage.id }}</router-link>
            </td>
            <td>{{ plantage.date }}</td>
            <td>{{ plantage.nbrPlate }}</td>
            <td>
              <span v-for="(plante, i) in plantage.plantes" :key="plante.id"
                >{{ i > 0 ? ', ' : '' }}
                <router-link class="form-control-static" :to="{ name: 'PlanteView', params: { planteId: plante.id } }">{{
                  plante.id
                }}</router-link>
              </span>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'PlantageView', params: { plantageId: plantage.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'PlantageEdit', params: { plantageId: plantage.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(plantage)"
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
        ><span id="pfaApp.plantage.delete.question" data-cy="plantageDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-plantage-heading" v-text="$t('pfaApp.plantage.delete.question', { id: removeId })">
          Are you sure you want to delete this Plantage?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-plantage"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removePlantage()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./plantage.component.ts"></script>
