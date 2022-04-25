<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="pfaApp.plante.home.createOrEditLabel"
          data-cy="PlanteCreateUpdateHeading"
          v-text="$t('pfaApp.plante.home.createOrEditLabel')"
        >
          Create or edit a Plante
        </h2>
        <div>
          <div class="form-group" v-if="plante.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="plante.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pfaApp.plante.lebelle')" for="plante-lebelle">Lebelle</label>
            <input
              type="text"
              class="form-control"
              name="lebelle"
              id="plante-lebelle"
              data-cy="lebelle"
              :class="{ valid: !$v.plante.lebelle.$invalid, invalid: $v.plante.lebelle.$invalid }"
              v-model="$v.plante.lebelle.$model"
              required
            />
            <div v-if="$v.plante.lebelle.$anyDirty && $v.plante.lebelle.$invalid">
              <small class="form-text text-danger" v-if="!$v.plante.lebelle.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pfaApp.plante.photo')" for="plante-photo">Photo</label>
            <input
              type="text"
              class="form-control"
              name="photo"
              id="plante-photo"
              data-cy="photo"
              :class="{ valid: !$v.plante.photo.$invalid, invalid: $v.plante.photo.$invalid }"
              v-model="$v.plante.photo.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pfaApp.plante.racin')" for="plante-racin">Racin</label>
            <input
              type="text"
              class="form-control"
              name="racin"
              id="plante-racin"
              data-cy="racin"
              :class="{ valid: !$v.plante.racin.$invalid, invalid: $v.plante.racin.$invalid }"
              v-model="$v.plante.racin.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pfaApp.plante.typePlant')" for="plante-typePlant">Type Plant</label>
            <select class="form-control" id="plante-typePlant" data-cy="typePlant" name="typePlant" v-model="plante.typePlant">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="plante.typePlant && typePlantOption.id === plante.typePlant.id ? plante.typePlant : typePlantOption"
                v-for="typePlantOption in typePlants"
                :key="typePlantOption.id"
              >
                {{ typePlantOption.id }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.plante.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./plante-update.component.ts"></script>
