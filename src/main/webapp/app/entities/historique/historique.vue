<template>
  <div>
    <h2 id="page-heading" data-cy="HistoriqueHeading">
      <span v-text="$t('pfaApp.historique.home.title')" id="historique-heading">Historiques</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('pfaApp.historique.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'HistoriqueCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-historique"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('pfaApp.historique.home.createLabel')"> Create a new Historique </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && historiques && historiques.length === 0">
      <span v-text="$t('pfaApp.historique.home.notFound')">No historiques found</span>
    </div>
    <div class="table-responsive" v-if="historiques && historiques.length > 0">
      <table class="table table-striped" aria-describedby="historiques">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('pfaApp.historique.dateArosage')">Date Arosage</span></th>
            <th scope="row"><span v-text="$t('pfaApp.historique.qttEau')">Qtt Eau</span></th>
            <th scope="row"><span v-text="$t('pfaApp.historique.parcelle')">Parcelle</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="historique in historiques" :key="historique.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'HistoriqueView', params: { historiqueId: historique.id } }">{{ historique.id }}</router-link>
            </td>
            <td>{{ historique.dateArosage }}</td>
            <td>{{ historique.qttEau }}</td>
            <td>
              <div v-if="historique.parcelle">
                <router-link :to="{ name: 'ParcelleView', params: { parcelleId: historique.parcelle.id } }">{{
                  historique.parcelle.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'HistoriqueView', params: { historiqueId: historique.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'HistoriqueEdit', params: { historiqueId: historique.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(historique)"
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
        ><span id="pfaApp.historique.delete.question" data-cy="historiqueDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-historique-heading" v-text="$t('pfaApp.historique.delete.question', { id: removeId })">
          Are you sure you want to delete this Historique?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-historique"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeHistorique()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./historique.component.ts"></script>
