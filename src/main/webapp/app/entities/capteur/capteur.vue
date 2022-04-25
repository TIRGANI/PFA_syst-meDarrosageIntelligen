<template>
  <div>
    <h2 id="page-heading" data-cy="CapteurHeading">
      <span v-text="$t('pfaApp.capteur.home.title')" id="capteur-heading">Capteurs</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('pfaApp.capteur.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'CapteurCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-capteur"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('pfaApp.capteur.home.createLabel')"> Create a new Capteur </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && capteurs && capteurs.length === 0">
      <span v-text="$t('pfaApp.capteur.home.notFound')">No capteurs found</span>
    </div>
    <div class="table-responsive" v-if="capteurs && capteurs.length > 0">
      <table class="table table-striped" aria-describedby="capteurs">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('pfaApp.capteur.type')">Type</span></th>
            <th scope="row"><span v-text="$t('pfaApp.capteur.image')">Image</span></th>
            <th scope="row"><span v-text="$t('pfaApp.capteur.description')">Description</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="capteur in capteurs" :key="capteur.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'CapteurView', params: { capteurId: capteur.id } }">{{ capteur.id }}</router-link>
            </td>
            <td>{{ capteur.type }}</td>
            <td>{{ capteur.image }}</td>
            <td>{{ capteur.description }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'CapteurView', params: { capteurId: capteur.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'CapteurEdit', params: { capteurId: capteur.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(capteur)"
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
        ><span id="pfaApp.capteur.delete.question" data-cy="capteurDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-capteur-heading" v-text="$t('pfaApp.capteur.delete.question', { id: removeId })">
          Are you sure you want to delete this Capteur?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-capteur"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeCapteur()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./capteur.component.ts"></script>
