//
//  LoginViewModel.swift
//  iosApp
//
//  Created by Auriane POIRIER on 13/12/2024.
//

import Foundation
import shared 

class LoginViewModel: ObservableObject {
    private let repository: LoginRepository

    // Propriétés observables pour la Vue
    @Published var loginMessage: String = ""
    @Published var userRole: String = ""

    init(repository: LoginRepository) {
        self.repository = repository
    }

    // Méthode pour gérer la connexion
    func login(email: String, password: String) {
        loginMessage = repository.authenticate(email: email, password: password)
        userRole = email.contains("admin") ? "admin" : "user"
    }
}
