Rails.application.routes.draw do
  # The priority is based upon order of creation: first created -> highest priority.
  # See how all your routes lay out with "rake routes".

  # You can have the root of your site routed with "root"
  # root 'welcome#index'

  # Example of regular route:
  #   get 'products/:id' => 'catalog#view'

  # Example of named route that can be invoked with purchase_url(id: product.id)
  #   get 'products/:id/purchase' => 'catalog#purchase', as: :purchase

  # Example resource route (maps HTTP verbs to controller actions automatically):
  #   resources :products

  # Example resource route with options:
  #   resources :products do
  #     member do
  #       get 'short'
  #       post 'toggle'
  #     end
  #
  #     collection do
  #       get 'sold'
  #     end
  #   end

  # Example resource route with sub-resources:
  #   resources :products do
  #     resources :comments, :sales
  #     resource :seller
  #   end

  # Example resource route with more complex sub-resources:
  #   resources :products do
  #     resources :comments
  #     resources :sales do
  #       get 'recent', on: :collection
  #     end
  #   end

  # Example resource route with concerns:
  #   concern :toggleable do
  #     post 'toggle'
  #   end
  #   resources :posts, concerns: :toggleable
  #   resources :photos, concerns: :toggleable

  # Example resource route within a namespace:
  #   namespace :admin do
  #     # Directs /admin/products/* to Admin::ProductsController
  #     # (app/controllers/admin/products_controller.rb)
  #     resources :products
  #   end
  resources :documents
	get 'documents/bold/:a/:b', to: 'documents#bold'
	get 'documents/italic/:a/:b', to: 'documents#italic'
	get 'documents/underlined/:a/:b', to: 'documents#underlined'
	get 'documents/striked/:a/:b', to: 'documents#striked'
	get 'documents/undoaction', to: 'documents#undoacton'
	get 'documents/redoactoin', to: 'documents#redoaction'
	get 'documents/table/:c', to: 'documents#table'
	get 'documents/font/:a/:b', to: 'documents#font'
	get 'documents/alignment/:a/:b', to: 'documents#alignment'
	get 'documents/indent/:a/:b', to: 'documents#indent'
	get 'documents/insert/:a/:b', to: 'documents#insert'
	get 'documents/deletebefore/:a/:b', to: 'documents#deletebefore'
	get 'documents/deleteafter/:a/:b', to: 'documents#deleteafter'
	get 'documents/table/:a/:b/:c', to: 'documents#table'
	post 'documents/new_document', to: 'documents#new_document'
end
