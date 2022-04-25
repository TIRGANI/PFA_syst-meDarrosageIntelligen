<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="pfaApp.parcelle.home.createOrEditLabel"
          data-cy="ParcelleCreateUpdateHeading"
          v-text="$t('pfaApp.parcelle.home.createOrEditLabel')"
        >
          Create or edit a Parcelle
        </h2>
        <div>
          <div class="form-group" v-if="parcelle.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="parcelle.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pfaApp.parcelle.surface')" for="parcelle-surface">Surface</label>
            <input
              type="number"
              class="form-control"
              name="surface"
              id="parcelle-surface"
              data-cy="surface"
              :class="{ valid: !$v.parcelle.surface.$invalid, invalid: $v.parcelle.surface.$invalid }"
              v-model.number="$v.parcelle.surface.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pfaApp.parcelle.photo')" for="parcelle-photo">Photo</label>
            <input
              type="text"
              class="form-control"
              name="photo"
              id="parcelle-photo"
              data-cy="photo"
              :class="{ valid: !$v.parcelle.photo.$invalid, invalid: $v.parcelle.photo.$invalid }"
              v-model="$v.parcelle.photo.$model"
            />
          </div>
          <div class="form-group">
            <label v-text="$t('pfaApp.parcelle.ferm')" for="parcelle-ferm">Ferm</label>
            <select
              class="form-control"
              id="parcelle-ferms"
              data-cy="ferm"
              multiple
              name="ferm"
              v-if="parcelle.ferms !== undefined"
              v-model="parcelle.ferms"
            >
              <option v-bind:value="getSelected(parcelle.ferms, fermOption)" v-for="fermOption in ferms" :key="fermOption.id">
                {{ fermOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label v-text="$t('pfaApp.parcelle.plantage')" for="parcelle-plantage">Plantage</label>
            <select
              class="form-control"
              id="parcelle-plantages"
              data-cy="plantage"
              multiple
              name="plantage"
              v-if="parcelle.plantages !== undefined"
              v-model="parcelle.plantages"
            >
              <option
                v-bind:value="getSelected(parcelle.plantages, plantageOption)"
                v-for="plantageOption in plantages"
                :key="plantageOption.id"
              >
                {{ plantageOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pfaApp.parcelle.typeSol')" for="parcelle-typeSol">Type Sol</label>
            <select class="form-control" id="parcelle-typeSol" data-cy="typeSol" name="typeSol" v-model="parcelle.typeSol">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="parcelle.typeSol && typeSolOption.id === parcelle.typeSol.id ? parcelle.typeSol : typeSolOption"
                v-for="typeSolOption in typeSols"
                :key="typeSolOption.id"
              >
                {{ typeSolOption.id }}
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
            :disabled="$v.parcelle.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./parcelle-update.component.ts"></script>
