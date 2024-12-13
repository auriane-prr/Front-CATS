//
//  LoginViewModelFactory.swift
//  iosApp
//
//  Created by Auriane POIRIER on 13/12/2024.
//

import Foundation
import shared 

class LoginViewModelFactory {
    private let repository: LoginRepository

    init(repository: LoginRepository) {
        self.repository = repository
    }

    func create() -> LoginViewModel {
        return LoginViewModel(repository: repository)
    }
}
